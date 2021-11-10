package org.decembrist.di

import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.isPrivate
import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.symbol.Modifier.*
import com.google.devtools.ksp.validate
import org.decembrist.di.annotations.Injectable
import org.decembrist.preprocessors.utils.*

class DiProcessor(
    private val codeGenerator: CodeGenerator,
    private val options: Map<String, String>,
) : SymbolProcessor {

    lateinit var context: Context
    lateinit var dependencyService: DependencyService

    override fun process(resolver: Resolver): List<KSAnnotated> {
        context = Context(resolver)
        dependencyService = DependencyService(context)
        resolver.getSymbolsWithAnnotation(Injectable::class.qualifiedName!!).forEach { annotated ->
            if (annotated.validate()) {
                when (annotated) {
                    is KSClassDeclaration -> annotated.processInjectable()
                    is KSFunctionDeclaration -> annotated.processInjectable()
                }
            }
        }
        if (context.size > 0) {
            dependencyService.resolveDependencies()
            val contextData = ContextData(
                dependencies = context.getDependencies(),
                options = ContextDataOptions(options[ROOT_PACKAGE] ?: ""),
            )
            ContextFilesGenerator(codeGenerator, contextData).generate()
        }

        return emptyList()
    }

    private fun KSClassDeclaration.processInjectable() {
        val className = getFullClassName()
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
        checkTopLevel(getFullName())
        checkModifiers(getFullName(), PRIVATE, SUSPEND)
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
