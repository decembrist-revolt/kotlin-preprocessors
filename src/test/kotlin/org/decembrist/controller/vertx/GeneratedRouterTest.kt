package org.decembrist.controller.vertx

import io.kotest.matchers.shouldBe
import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.*
import org.decembrist.vertx.annotations.Controller
import org.decembrist.vertx.annotations.GET
import org.decembrist.vertx.annotations.REQUEST
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(VertxExtension::class)
class GeneratedRouterTest {

    private var server: HttpServer? = null

    @AfterEach
    fun tearDown() {
        server?.close()
    }

    @Test
    @Timeout(5000L)
    fun `success path`(vertx: Vertx, testContext: VertxTestContext): Unit = runBlocking {
        val checkpoint = testContext.checkpoint(2)
        val job = launch {
            val router = Router.router(vertx)
            router.mountSubRouter("/", TestController1().asRouter(vertx))
            router.mountSubRouter("/", TestController2().asRouter(vertx))
            val port = 8080
            val host = "localhost"
            server = vertx.createHttpServer()
                .requestHandler(router)
                .listen(port, host).await()
            val client = vertx.createHttpClient()
            var response = client
                .request(HttpMethod.GET, port, host, "/base/path")
                .await().send().await().body().await()
            response.toString() shouldBe "METHOD GET OK"
            checkpoint.flag()
            response = client
                .request(HttpMethod.PUT, port, host, "/path")
                .await().send().await().body().await()
            response.toString() shouldBe "REQUEST PUT OK"
            checkpoint.flag()
        }
        delay(5000L)
        server?.close()
        job.cancelAndJoin()
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