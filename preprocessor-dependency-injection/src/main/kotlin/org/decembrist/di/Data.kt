package org.decembrist.di

import com.google.devtools.ksp.symbol.KSFile

data class ContextData(
    val dependencies: List<Dependency>,
    val options: ContextDataOptions = ContextDataOptions(""),
)

data class ContextDataOptions(
    val rootPackage: String,
)

class ExternalDependency(
    type: String,
    name: String? = null,
) : Dependency(type, emptyList(), name = name, resolved = true)

class FunctionalDependency(
    type: String,
    superTypes: List<String>,
    injected: List<Injected> = emptyList(),
    name: String? = null,
    file: KSFile,
    resolved: Boolean = false,
    order: Int = -1,
    val funcName: String,
) : Dependency(type, superTypes, injected, name, file, resolved, order)

open class Dependency(
    val type: String,
    val superTypes: List<String>,
    val injected: List<Injected> = emptyList(),
    var name: String? = null,
    val file: KSFile? = null,
    var resolved: Boolean = false,
    var order: Int = -1,
) {
    override fun toString() = "$type(${injected.joinToString(", ")})"
}

data class Injected(
    val type: String,
    val name: String? = null,
    val external: Boolean? = null,
) {
    lateinit var dependency: Dependency

    override fun toString() = if (name != null) "$name: $type" else type
}