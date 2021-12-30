package org.decembrist.controller

import com.google.devtools.ksp.symbol.*
import org.decembrist.preprocessors.utils.*
import org.decembrist.vertx.annotations.*

fun KSFunctionDeclaration.getRouteData(): List<RouteData> {
    val methodRoutes: List<RouteData> = getMethodAnnotations().map { methodAnnotation ->
        val path = methodAnnotation.getPathArgument(getFullName())
        RouteData(
            path = path,
            methodName = getSimpleName(),
            methods = listOf(methodAnnotation.shortName.asString()),
        )
    }
    val requestRoutes: Sequence<RouteData> = getAnnotationsOfType<REQUEST>().map { requestAnnotation ->
        val path = requestAnnotation.getPathArgument(getFullName())
        RouteData(
            path = path,
            methodName = getSimpleName(),
            methods = requestAnnotation.getArgumentValue(REQUEST::method.name),
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
