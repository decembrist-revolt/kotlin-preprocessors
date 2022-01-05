package org.decembrist.controller

import org.decembrist.vertx.annotations.BodyHandler

data class RouterData(
    var parentPath: String,
    var packageName: String,
    var controllerClass: String,
    val routes: List<RouteData> = emptyList(),
    val bodyHandler: Boolean = false,
)

data class RouteData(
    val path: String,
    val methodName: String,
    val methods: List<String> = emptyList(),
    val bodyHandler: BodyHandler = BodyHandler(),
)