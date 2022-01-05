import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler

fun BodyHandlerController.asRouter(vertx: Vertx): Router {
    val router = Router.router(vertx)
    router.route("/get")
        .method(HttpMethod("GET"))
        .handler(this::getWithoutBH)
    router.route("/")
        .method(HttpMethod("POST"))
        .handler(this::postWithoutBH)
    router.route("/")
        .method(HttpMethod("PUT"))
        .method(HttpMethod("OPTIONS"))
        .handler(this::requestWithoutBH)
    router.route("/get")
        .method(HttpMethod("GET"))
        .handler(BodyHandler.create())
        .handler(this::getWithBH)
    router.route("/")
        .method(HttpMethod("POST"))
        .handler(BodyHandler.create())
        .handler(this::postWithBH)
    router.route("/")
        .method(HttpMethod("PUT"))
        .method(HttpMethod("OPTIONS"))
        .handler(BodyHandler.create())
        .handler(this::requestWithBH)
    return router
}