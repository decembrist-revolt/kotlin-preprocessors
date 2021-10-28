package org.decembrist

import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import org.decembrist.vertx.annotations.Controller
import org.decembrist.vertx.annotations.GET
import org.decembrist.vertx.annotations.REQUEST

fun main() {
    val vertx = Vertx.vertx()
    val router: Router = HelloController().asRouter(vertx)

    vertx.createHttpServer()
        .requestHandler(router)
        .listen(80) {
            if (it.succeeded()) {
                println("Server started!")
            } else {
                println(it.cause())
            }
        }
}

@Controller("/parent")
class HelloController {
    @GET("/hello") //or @REQUEST(method = ["GET"], "/hello")
    fun hello(ctx: RoutingContext) {
        ctx.response().send("Hello World")
    }
}