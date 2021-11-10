package org.decembrist.controller

data class RouterData(
    var parentPath: String,
    var packageName: String,
    var controllerClass: String,
    val routes: List<RouteData> = emptyList(),
)

data class RouteData(
    val path: String,
    val methodName: String,
    val methods: List<String> = emptyList(),
)