package org.decembrist.di

import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSModifierListOwner
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.Modifier
import com.google.devtools.ksp.symbol.Modifier.ABSTRACT
import com.google.devtools.ksp.symbol.Modifier.PRIVATE
import com.google.devtools.ksp.symbol.Modifier.SUSPEND
import com.google.devtools.ksp.validate
import org.decembrist.di.annotations.Injectable
import org.decembrist.preprocessors.utils.findFirstModifierOrNull
import org.decembrist.preprocessors.utils.getAnnotationOfType
import org.decembrist.preprocessors.utils.getArgumentValue
import org.decembrist.preprocessors.utils.getFullClassName
import org.decembrist.preprocessors.utils.getFullName
import org.decembrist.preprocessors.utils.isTopLevel

class DiProcessor(
    private val codeGenerator: CodeGenerator,
    private val options: Map<String, String>,
    private val logger: KSPLogger,
) : SymbolProcessor {

    lateinit var context: Context
    lateinit var dependencyService: DependencyService

    override fun process(resolver: Resolver): List<KSAnnotated> {
        context = Context(resolver, logger)
        dependencyService = DependencyService(context, logger)
        resolver.getSymbolsWithAnnotation(Injectable::class.qualifiedName!!).forEach { annotated ->
            if (annotated.validate()) {
                when (annotated) {
                    is KSClassDeclaration -> annotated.processInjectable()
                    is KSFunctionDeclaration -> annotated.processInjectable()
                }
            } else throw MalformedInjectableError(annotated.toString())
        }
        if (context.size > 0) {
            dependencyService.resolveDependencies()
            val contextData = ContextData(
                dependencies = context.getDependencies(),
                options = ContextDataOptions(options[ROOT_PACKAGE] ?: ""),
            )
            ContextFilesGenerator(codeGenerator, contextData, logger).generate()
        }

        return emptyList()
    }

    private fun KSClassDeclaration.processInjectable() {
        val className = getFullClassName()
        if (typeParameters.isNotEmpty()) throw GenericInjectableError(className)
        checkTopLevel(className)
        if (classKind != ClassKind.CLASS) throw WrongInjectableTargetError(classKind.type, className)
        checkModifiers(className, PRIVATE, ABSTRACT)
        val constructors = getConstructors().toList()
        if (constructors.size > 1 || !constructors.first().isPublic()) {
            throw AmbiguousConstructorError(className)
        }
        val dependencyName = getDependencyName()
        context.addNew(this, dependencyName)
    }

    private fun KSFunctionDeclaration.processInjectable() {
        val fullName = getFullName()
        if (typeParameters.isNotEmpty()) throw GenericInjectableFuncError(fullName)
        checkTopLevel(fullName)
        checkModifiers(fullName, PRIVATE, SUSPEND)
        val dependencyName = getDependencyName()
        context.addNew(this, dependencyName)
    }

    private fun KSNode.checkTopLevel(type: String) {
        if (!isTopLevel()) throw NonTopLevelInjectable(type)
    }

    private fun KSModifierListOwner.checkModifiers(target: String, vararg modifiers: Modifier) {
        val incorrectModifier = findFirstModifierOrNull(*modifiers)
        if (incorrectModifier != null) {
            throw WrongTargetModifierError(incorrectModifier, target)
        }
    }

    private fun KSAnnotated.getDependencyName(): String? {
        val injectable = getAnnotationOfType<Injectable>()
        val name = injectable.getArgumentValue(Injectable::name).takeIf { it.isNotBlank() }
        if (name != null
            && name.first().digitToIntOrNull() != null
            && !name.matches(Regex("[a-zA-Z0-9]+"))
        ) {
            throw WrongDependencyNameError(name)
        }
        return name
    }

    companion object {
        const val ROOT_PACKAGE = "rootPackage"
    }
}
