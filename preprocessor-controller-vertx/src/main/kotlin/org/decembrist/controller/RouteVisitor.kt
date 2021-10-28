package org.decembrist.controller

import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.visitor.KSEmptyVisitor
import org.decembrist.getAnnotationsOfType
import org.decembrist.getArgumentValue
import org.decembrist.vertx.annotations.*
import org.koin.core.component.KoinComponent

class RouteVisitor : KSEmptyVisitor<RouterData, Unit>(), KoinComponent {

    override fun defaultHandler(node: KSNode, data: RouterData) {
    }

    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: RouterData) {
        val methodName = function.funcName()
        val methodRoutes = function.getMethodAnnotations().map { methodAnnotation ->
            val path = methodAnnotation.getPathArgument(function.className(), function.funcName())
            RouteData(
                path = path,
                methodName = methodName,
                methods = listOf(methodAnnotation.shortName.asString()),
            )
        }
        val requestRoutes = function.getAnnotationsOfType(REQUEST::class).map { requestAnnotation ->
            val path = requestAnnotation.getPathArgument(function.className(), function.funcName())
            RouteData(
                path = path,
                methodName = methodName,
                methods = requestAnnotation.getArgumentValue(REQUEST::method.name),
            )
        }
        val routes = methodRoutes + requestRoutes
        if (routes.isNotEmpty()) {
            function.checkParameters()
            data.routes.addAll(routes)
        }
    }

    private fun KSAnnotation.getPathArgument(className: String, funcName: String) =
        getArgumentValue<String>(REQUEST::path.name)
            .takeIf { it.isNotBlank() } ?: error(
            "Annotation @${shortName.asString()} has empty path argument " +
                    "for method ${className}.${funcName}()"
        )

    private fun KSFunctionDeclaration.getMethodAnnotations(): List<KSAnnotation> =
        ROUTE_ANNOTATIONS.flatMap { getAnnotationsOfType(it) }

    private fun KSFunctionDeclaration.checkParameters() {
        if (parameters.size != 1) {
            error(
                "Method ${className()}.${funcName()}() " +
                        "should have only one io.vertx.ext.web.RoutingContext parameter"
            )
        }
        val parameter = parameters.first()
        if (parameter.type.resolve().declaration.qualifiedName?.asString() != "io.vertx.ext.web.RoutingContext") {
            val paramName = parameter.name!!.asString()
            error(
                "Method ${className()}.${funcName()}($paramName) " +
                        "should have only one io.vertx.ext.web.RoutingContext parameter"
            )
        }
    }

    private fun KSFunctionDeclaration.funcName() = simpleName.asString()

    private fun KSFunctionDeclaration.className() = (parent as KSClassDeclaration).qualifiedName!!.asString()

    companion object {
        val ROUTE_ANNOTATIONS = listOf(
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
    }

}