import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.core.http.HttpMethod

fun BasePathController2.asRouter(vertx: Vertx): Router {
    val router = Router.router(vertx)
    router.route("/get")
        .method(HttpMethod("GET"))
        .handler(this::get)
    router.route("/")
        .method(HttpMethod("POST"))
        .handler(this::post)
    router.route("/")
        .method(HttpMethod("PUT"))
        .handler(this::put)
    return router
}