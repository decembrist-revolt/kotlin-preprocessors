package org.decembrist.controller

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import org.decembrist.preprocessors.utils.getAnnotationArgumentAsMap
import org.decembrist.preprocessors.utils.getAnnotationsOfType
import org.decembrist.preprocessors.utils.getArgumentValue
import org.decembrist.preprocessors.utils.getFullName
import org.decembrist.preprocessors.utils.getSimpleName
import org.decembrist.vertx.annotations.ACL
import org.decembrist.vertx.annotations.ALL
import org.decembrist.vertx.annotations.BASELINE_CONTROL
import org.decembrist.vertx.annotations.BodyHandler
import org.decembrist.vertx.annotations.CHECKIN
import org.decembrist.vertx.annotations.CHECKOUT
import org.decembrist.vertx.annotations.CONNECT
import org.decembrist.vertx.annotations.COPY
import org.decembrist.vertx.annotations.DELETE
import org.decembrist.vertx.annotations.GET
import org.decembrist.vertx.annotations.HEAD
import org.decembrist.vertx.annotations.LABEL
import org.decembrist.vertx.annotations.LOCK
import org.decembrist.vertx.annotations.MERGE
import org.decembrist.vertx.annotations.MKACTIVITY
import org.decembrist.vertx.annotations.MKCALENDAR
import org.decembrist.vertx.annotations.MKCOL
import org.decembrist.vertx.annotations.MKWORKSPACE
import org.decembrist.vertx.annotations.MOVE
import org.decembrist.vertx.annotations.OPTIONS
import org.decembrist.vertx.annotations.ORDERPATCH
import org.decembrist.vertx.annotations.PATCH
import org.decembrist.vertx.annotations.POST
import org.decembrist.vertx.annotations.PROPFIND
import org.decembrist.vertx.annotations.PROPPATCH
import org.decembrist.vertx.annotations.PUT
import org.decembrist.vertx.annotations.REPORT
import org.decembrist.vertx.annotations.REQUEST
import org.decembrist.vertx.annotations.SEARCH
import org.decembrist.vertx.annotations.TRACE
import org.decembrist.vertx.annotations.UNCHECKOUT
import org.decembrist.vertx.annotations.UNLOCK
import org.decembrist.vertx.annotations.UPDATE
import org.decembrist.vertx.annotations.VERSION_CONTROL

fun KSFunctionDeclaration.getRouteData(): List<RouteData> {
    val methodRoutes: List<RouteData> = getMethodAnnotations().map { methodAnnotation ->
        val path: String = methodAnnotation.getPathArgument(getFullName())
        val bodyHandler: BodyHandler = methodAnnotation.getBodyHandlerArgument()
        RouteData(
            path = path,
            methodName = getSimpleName(),
            methods = listOf(methodAnnotation.shortName.asString()),
            bodyHandler = bodyHandler,
        )
    }
    val requestRoutes: Sequence<RouteData> = getAnnotationsOfType<REQUEST>().map { requestAnnotation ->
        val path: String = requestAnnotation.getPathArgument(getFullName())
        val bodyHandler: BodyHandler = requestAnnotation.getBodyHandlerArgument()
        RouteData(
            path = path,
            methodName = getSimpleName(),
            methods = requestAnnotation.getArgumentValue(REQUEST::method.name),
            bodyHandler = bodyHandler,
        )
    }
    val routes: List<RouteData> = methodRoutes + requestRoutes
    return if (routes.isNotEmpty()) {
        checkRouteMethodParameters()
        routes
    } else emptyList()
}

private fun KSAnnotation.getPathArgument(funcName: String): String {
    val path = getArgumentValue<String>(REQUEST::path.name)
        .takeIf { it.isNotBlank() } ?: error(
        "Annotation @${shortName.asString()} has empty path argument " +
                "for method ${funcName}()"
    )
    return if (path == "/") "" else path
}

private fun KSAnnotation.getBodyHandlerArgument(): BodyHandler {
    val bodyHandler: Map<String, Any?> = getAnnotationArgumentAsMap(REQUEST::bodyHandler.name)
    return BodyHandler(enabled = bodyHandler[BodyHandler::enabled.name] as Boolean)
}

private fun KSFunctionDeclaration.getMethodAnnotations(): List<KSAnnotation> =
    ROUTE_ANNOTATIONS.flatMap { getAnnotationsOfType(it) }

private val ROUTE_ANNOTATIONS = listOf(
    OPTIONS::class,
    GET::class,
    HEAD::class,
    POST::class,
    PUT::class,
    DELETE::class,
    TRACE::class,
    CONNECT::class,
    PATCH::class,
    PROPFIND::class,
    PROPPATCH::class,
    MKCOL::class,
    COPY::class,
    MOVE::class,
    LOCK::class,
    UNLOCK::class,
    MKCALENDAR::class,
    VERSION_CONTROL::class,
    REPORT::class,
    CHECKOUT::class,
    CHECKIN::class,
    UNCHECKOUT::class,
    MKWORKSPACE::class,
    UPDATE::class,
    LABEL::class,
    MERGE::class,
    BASELINE_CONTROL::class,
    MKACTIVITY::class,
    ORDERPATCH::class,
    ACL::class,
    SEARCH::class,
    ALL::class,
)
