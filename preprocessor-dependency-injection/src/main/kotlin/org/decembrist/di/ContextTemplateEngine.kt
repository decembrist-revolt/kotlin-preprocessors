package org.decembrist.di

import com.google.devtools.ksp.processing.KSPLogger
import org.decembrist.preprocessors.generator.AbstractTemplateEngine
import java.io.OutputStream

class ContextTemplateEngine(
    file: OutputStream,
    private val logger: KSPLogger,
) : AbstractTemplateEngine<ContextData>(file) {

    override fun renderBody(data: ContextData) {
        writePackageLine(data.options.rootPackage)
        writeLine {
            """
            |import kotlin.reflect.KFunction
            |
            |val diContext: Map<String, Any> by lazy {
            |    val data = mutableMapOf<String, Any>()
            """.trimMargin()
        }
        renderDependencies(data)
        writeLine {
            """
            |    data
            |}
        """.trimMargin()
        }
        nextLine()
        renderGetInstance()
    }

    private fun renderDependencies(data: ContextData) {
        val names: MutableSet<String> = data.dependencies.mapNotNull(Dependency::name).toMutableSet()
        data.dependencies
            .distinctBy { it.name to it.type }
            .sortedBy { it.order }
            .forEach { dependency ->
                val dependencyKey = dependency.name ?: dependency.type
                calculateUniqueName(names, dependency)
                val instanceString = instantiateDependencyString(dependency)
                val dataValue = if (dependency is FunctionalDependency) {
                    "{ $instanceString }"
                } else {
                    writeLine { "    val ${dependency.name} = $instanceString" }
                    dependency.name
                }
                writeLine { "    data[\"$dependencyKey\"] = $dataValue" }
            }
    }

    private fun instantiateDependencyString(dependency: Dependency): String = with(dependency) {
        val paramsString = injected
            .map(Injected::dependency)
            .joinToString(transform = ::paramDependencyString)
        return if (dependency is FunctionalDependency) {
            "${dependency.funcName}($paramsString)"
        } else {
            "$type($paramsString)"
        }
    }

    private fun paramDependencyString(dependency: Dependency): String = if (dependency is FunctionalDependency) {
        instantiateDependencyString(dependency)
    } else {
        dependency.name!!
    }


    /**
     * Calculates new unique name for dependency if dependency name == null
     */
    private fun calculateUniqueName(names: MutableSet<String>, dependency: Dependency) {
        if (dependency.name == null) {
            var name = dependency.type.replace(".", "")
            while (names.contains(name)) {
                name += 1
            }
            dependency.name = name
            names += name
        }
    }

    private fun renderGetInstance() = writeLine {
        """
            |/**
            | * @param name T class qualifiedName by default (if @Injectable name was not specified)
            | * @return injectable by name
            | */
            |inline fun <reified T: Any> getInstance(name: String = T::class.qualifiedName!!): T {
            |    val instance = diContext[name] ?: throw RuntimeException("@Injectable by name ${'$'}name not found")
            |    return if (instance !is T) {
            |        (instance as (() -> T))()
            |    } else instance
            |}
        """.trimMargin()
    }

}