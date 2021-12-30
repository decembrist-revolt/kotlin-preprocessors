package org.decembrist

import org.decembrist.controller.RouteData
import org.decembrist.controller.RouterData
import org.decembrist.preprocessors.generator.AbstractTemplateEngine
import java.io.OutputStream

class RouterTemplateEngine(file: OutputStream) : AbstractTemplateEngine<RouterData>(file) {

    override fun renderBody(data: RouterData) = with(data) {
        writePackageLine(packageName)
        write {
            """      
            |import io.vertx.core.Vertx
            |import io.vertx.ext.web.Router
            |import io.vertx.core.http.HttpMethod
            |
            |fun ${controllerClass}.asRouter(vertx: Vertx): Router {
            |    val router = Router.router(vertx)
            """.trimMargin()
        }
        nextLine()
        renderRoutes(data)
        write {
            """
            |    return router
            |}
            """.trimMargin()
        }
    }

    private fun renderRoutes(data: RouterData) {
        data.routes
            .map { route ->
                if (data.parentPath.isNotBlank()) {
                    route.copy(path = combinePaths(data.parentPath, route.path))
                } else if (route.path == "") {
                    route.copy(path = "/")
                } else route
            }.forEach { route -> renderRoute(route) }
    }

    private fun combinePaths(parentPath: String, path2: String): String {
        val correctParentPath = "/" + parentPath.trim('/')
        return if (path2 == "") correctParentPath else "$correctParentPath/${path2.trim('/')}"
    }

    private fun renderRoute(route: RouteData): Unit = with(route) {
        write { """    router.route("$path")""" }
        nextLine()
        for (method in methods) {
            write { """        .method(HttpMethod("$method"))""" }
            nextLine()
        }
        write { """        .handler(this::$methodName)""" }
        nextLine()
    }


}