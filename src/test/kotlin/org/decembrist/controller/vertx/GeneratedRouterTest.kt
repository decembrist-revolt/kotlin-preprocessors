package org.decembrist.controller.vertx

import io.kotest.matchers.shouldBe
import io.vertx.core.Vertx
import io.vertx.core.http.HttpClientRequest
import io.vertx.core.http.HttpClientResponse
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.decembrist.vertx.annotations.BodyHandler
import org.decembrist.vertx.annotations.Controller
import org.decembrist.vertx.annotations.GET
import org.decembrist.vertx.annotations.POST
import org.decembrist.vertx.annotations.REQUEST
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.net.ServerSocket
import java.util.concurrent.TimeUnit

@ExtendWith(VertxExtension::class)
class GeneratedRouterTest {

    @Test
    fun `success path`(vertx: Vertx, testContext: VertxTestContext) {
        val checkpoint = testContext.checkpoint(2)
        val router = Router.router(vertx)
        router.mountSubRouter("/", TestController1().asRouter(vertx))
        router.mountSubRouter("/", TestController2().asRouter(vertx))
        router.mountSubRouter("/", TestController3().asRouter(vertx))
        router.mountSubRouter("/", TestController4().asRouter(vertx))
        val socket = ServerSocket(0)
        socket.reuseAddress = true
        val port = socket.localPort
        socket.close()
        val host = "localhost"
        vertx.createHttpServer()
            .requestHandler(router)
            .listen(port, host)
            .onComplete {
                val server = it.result()
                val client = vertx.createHttpClient()
                client
                    .request(HttpMethod.GET, server.actualPort(), host, "/base/path")
                    .compose(HttpClientRequest::send)
                    .compose(HttpClientResponse::body)
                    .onComplete { result ->
                        result.result().toString() shouldBe "METHOD GET OK"
                        checkpoint.flag()
                    }
                client
                    .request(HttpMethod.PUT, server.actualPort(), host, "/path")
                    .compose(HttpClientRequest::send)
                    .compose(HttpClientResponse::body)
                    .onComplete { result ->
                        result.result().toString() shouldBe "REQUEST PUT OK"
                        checkpoint.flag()
                    }
                client
                    .request(HttpMethod.POST, server.actualPort(), host, "/")
                    .compose(HttpClientRequest::send)
                    .compose(HttpClientResponse::body)
                    .onComplete { result ->
                        result.result().toString() shouldBe "REQUEST PUT OK"
                        checkpoint.flag()
                    }
                client
                    .request(HttpMethod.POST, server.actualPort(), host, "/")
                    .compose { request -> request.send("hello") }
                    .compose(HttpClientResponse::body)
                    .onComplete { result ->
                        result.result().toString() shouldBe "REQUEST PUT OK hello"
                        checkpoint.flag()
                    }
            }
        testContext.awaitCompletion(5, TimeUnit.SECONDS) shouldBe true
        if (testContext.failed()) throw testContext.causeOfFailure()
    }


}

@Controller("base")
class TestController1 {
    @GET("/path")
    fun method(context: RoutingContext): String {
        context.response().send("METHOD GET OK")
        return ""
    }
}

@Controller
class TestController2 {
    @REQUEST(method = ["PUT"], path = "/path")
    fun request(ctx: RoutingContext) {
        ctx.response().send("REQUEST PUT OK")
    }
}

@Controller
class TestController3 {
    @POST
    fun request(ctx: RoutingContext) {
        ctx.response().send("REQUEST POST OK")
    }
}

@Controller
class TestController4 {
    @POST(bodyHandler = BodyHandler(true))
    fun request(ctx: RoutingContext) {
        ctx.response().send("REQUEST POST OK ${ctx.bodyAsString}")
    }
}