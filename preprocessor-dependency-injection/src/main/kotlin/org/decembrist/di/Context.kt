package org.decembrist.di

import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.symbol.Variance
import org.decembrist.di.annotations.Inject
import org.decembrist.preprocessors.utils.*

class Context(private val resolver: Resolver) {
    private val data = linkedMapOf<String, Dependency>()

    /* Map(type, superTypes) */
    private val superTypesMap = mutableMapOf<String, List<String>>()

    private var order: Int = 0

    val size get() = data.size

    fun addNew(clazz: KSClassDeclaration, dependencyName: String?) {
        val className = clazz.getFullClassName()
        data.compute(dependencyName ?: className) { name, oldValue ->
            if (oldValue != null) {
                error("Duplicate dependency name $name")
            }
            val parameters: List<KSValueParameter> = clazz.getConstructors().firstOrNull()?.parameters ?: emptyList()
            val injectedParams = getInjectedParams(parameters)
            val superTypes = superTypesMap[className] ?: resolver.getSuperTypes(className).also { superTypes ->
                superTypesMap[className] = superTypes
            }
            val isResolved = injectedParams.isEmpty()
            Dependency(
                type = className,
                superTypes = superTypes,
                injected = injectedParams,
                name = dependencyName,
                file = clazz.containingFile!!,
                resolved = isResolved,
                order = if (isResolved) order++ else -1
            )
        }
    }

    fun addNew(func: KSFunctionDeclaration, dependencyName: String?) {
        val className = func.returnType?.getClassName() ?: throw WrongInjectableTypeError(Unit::class.qualifiedName!!)
        if (className in RESTRICTED_TYPES) throw WrongInjectableTypeError(className)
        resolver.getClassDeclarationByName(className) ?: throw WrongInjectableTypeError(className)
        data.compute(dependencyName ?: className) { name, oldValue ->
            if (oldValue != null) {
                error("Duplicate dependency name $name")
            }
            val injectedParams = getInjectedParams(func.parameters)
            val superTypes = superTypesMap[className] ?: resolver.getSuperTypes(className).also { superTypes ->
                superTypesMap[className] = superTypes
            }
            val isResolved = injectedParams.isEmpty()
            FunctionalDependency(
                type = className,
                superTypes = superTypes,
                injected = injectedParams,
                name = dependencyName,
                file = func.containingFile!!,
                resolved = isResolved,
                order = if (isResolved) order++ else -1,
                funcName = func.getFullName()
            )
        }
    }

    operator fun set(dependencyName: String, dependency: Dependency) {
        data[dependencyName] = dependency
    }

    operator fun get(dependencyName: String) = data[dependencyName]

    fun getDependenciesByType(searchType: String): Collection<Dependency> =
        data.filterValues { it.type == searchType }.values

    fun getDependencies(): List<Dependency> = data.values.toList()

    fun getDependenciesBySuperType(superType: String): Collection<Dependency> =
        data.filterValues { superType in it.superTypes }.values

    fun getSuperTypesByType(type: String): List<String> = superTypesMap[type] ?: emptyList()

    fun markResolved(dependency: Dependency) {
        dependency.resolved = true
        dependency.order = order++
    }

    private fun Resolver.getSuperTypes(className: String): List<String> {
        val ksClass = getClassDeclarationByName(className) ?: error("Class [$className] not found")
        return ksClass.superTypes
            .map { it.getClassName() }
            .flatMap { getSuperTypes(it) + it }
            .toList()
    }

    private fun getInjectedParams(parameters: List<KSValueParameter>): List<Injected> = parameters.map { param ->
        val type = param.type
        val name = param.getAnnotationOfTypeOrNull<Inject>()
            ?.getArgumentValue(Inject::name)
            ?.takeIf { it.isNotBlank() }
        val typeArguments = type.element!!.typeArguments
        if (typeArguments.any { it.variance != Variance.STAR }) {
            throw GenericInjectedError(type.getClassName()
                    + typeArguments.joinToString(prefix = "<", postfix = ">") { it.toString() })
        }
        Injected(
            name = name,
            type = type.getClassName(),
        )
    }

    companion object {
        val RESTRICTED_TYPES = arrayOf("kotlin.Any", "java.lang.Object")
    }
}