package org.decembrist.controller

data class RouterData(
    var packageName: String? = null,
    var controllerClass: String? = null,
    val routes: MutableList<RouteData> = mutableListOf(),
    var parentPath: String,
)

data class RouteData(
    val path: String,
    val methodName: String,
    val methods: List<String> = emptyList(),
)