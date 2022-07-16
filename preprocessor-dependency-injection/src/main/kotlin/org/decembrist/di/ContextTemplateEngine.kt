package org.decembrist.di

import com.google.devtools.ksp.processing.KSPLogger
import org.decembrist.preprocessors.generator.AbstractTemplateEngine
import org.intellij.lang.annotations.Language
import java.io.OutputStream

class ContextTemplateEngine(
    file: OutputStream,
    private val logger: KSPLogger,
) : AbstractTemplateEngine<ContextData>(file) {

    override fun renderBody(data: ContextData) {
        logger.info("start build Context file")
        writePackageLine(data.options.rootPackage)
        renderHeader()
        nextLine()
        renderGetInstance()
        nextLine()
        renderDependencies(data)
        nextLine()
        logger.info("Context file built")
    }

    private fun renderHeader() {
        @Language("kotlin")
        val fileHeader = """
                |val diContext: Map<String, Any> get() = if (contextBuilt) _diContext else emptyMap()
                |var contextBuilt = false
                |
                |private lateinit var _diContext: Map<String, Any>
                """.trimMargin()
        writeLine { fileHeader }
    }

    private fun renderDependencies(data: ContextData) {
        @Language("kotlin")
        val startBlock = """
            |/**
            | * Build dependencies map (could be used as parent context)
            | */
            |fun buildContext(parentContext: Map<String, Any> = emptyMap()): Map<String, Any> {
            |    val data = parentContext.toMutableMap()
            """.trimMargin()
        writeLine { startBlock }
        val names: MutableSet<String> = data.dependencies.mapNotNull(Dependency::name).toMutableSet()
        val dependencies = data.dependencies.distinctBy { it.name to it.type }.sortedBy { it.order }
        for (dependency in dependencies) {
            if (dependency is ExternalDependency) continue
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
        @Language("kotlin")
        val returnBlock = """
            |    return data
            |}
            """.trimMargin()
        writeLine { returnBlock }
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
    } else if (dependency is ExternalDependency) {
        with(dependency) { """getOrInvoke<$type>("${name!!}", data["$name"])""" }
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

    private fun renderGetInstance() {
        @Language("kotlin")
        val block = """
                |/**
                | * @param name T class qualifiedName by default (if @Injectable name was not specified)
                | * @return injectable by name
                | */
                |@Suppress("UNCHECKED_CAST")
                |inline fun <reified T : Any> getInstance(name: String = T::class.qualifiedName!!,
                |                                         parentContext: Map<String, Any>? = null): T {
                |    val instance = resolveInstance<T>(name, parentContext)
                |    return getOrInvoke(name, instance)
                |}
                |
                |fun <T> resolveInstance(name: String, parentContext: Map<String, Any>? = null): Any? {
                |    if (!contextBuilt) {
                |        _diContext = buildContext(parentContext ?: emptyMap())
                |        contextBuilt = true
                |    }
                |    return diContext[name]
                |}
                |
                |inline fun <reified T : Any> getOrInvoke(name: String, instance: Any?): T {
                |    instance ?: throw RuntimeException("@Injectable by name ${'$'}name not found")
                |    return if (instance !is T) (instance as (() -> T))() else instance
                |}
            """.trimMargin()
        writeLine { block }
    }

}