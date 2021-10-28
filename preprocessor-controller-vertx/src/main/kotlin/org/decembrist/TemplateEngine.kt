package org.decembrist

import org.decembrist.controller.RouteData
import org.decembrist.controller.RouterData
import java.io.OutputStream

class TemplateEngine {

    fun renderRouter(file: OutputStream, data: RouterData): Unit = with(data) {
        file + getPackageLine(packageName)
        file.nextLine(2)
        file + """      
        |import io.vertx.core.Vertx
        |import io.vertx.ext.web.Router
        |import io.vertx.core.http.HttpMethod
        |
        |fun ${controllerClass}.asRouter(vertx: Vertx): Router {
        |    val router = Router.router(vertx)
        """.trimMargin()
        file.nextLine()

        data.routes
            .map { route ->
                if (parentPath.isNotBlank()) {
                    route.copy(path = combinePaths(parentPath, route.path))
                } else {
                    route
                }
            }
            .forEach { route -> renderRoute(file, route) }

        file + """
        |    return router
        |}
        """.trimMargin()
    }

    private fun getPackageLine(packageName: String?) = if (packageName.isNullOrBlank()) "" else "package $packageName"

    private fun combinePaths(path1: String, path2: String) = "/${path1.trim('/')}/${path2.trim('/')}"

    private fun renderRoute(file: OutputStream, route: RouteData): Unit = with(route) {
        file + """    router.route("$path")"""
        file.nextLine()
        for (method in methods) {
            file + """        .method(HttpMethod("$method"))"""
            file.nextLine()
        }
        file + """        .handler(this::$methodName)"""
        file.nextLine()
    }

    private operator fun OutputStream.plus(content: String) = write(content.toByteArray())

    private fun OutputStream.nextLine(repeat: Int = 1) = write(System.lineSeparator().repeat(repeat).toByteArray())
}