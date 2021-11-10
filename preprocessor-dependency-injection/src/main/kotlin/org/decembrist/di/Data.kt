package org.decembrist.di

import com.google.devtools.ksp.symbol.KSFile

data class ContextData(
    val dependencies: List<Dependency>,
    val options: ContextDataOptions = ContextDataOptions(""),
)

data class ContextDataOptions(
    val rootPackage: String,
)

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
    val file: KSFile,
    var resolved: Boolean = false,
    var order: Int = -1,
)

data class Injected(
    val type: String,
    val name: String? = null,
) {
    lateinit var dependency: Dependency
}