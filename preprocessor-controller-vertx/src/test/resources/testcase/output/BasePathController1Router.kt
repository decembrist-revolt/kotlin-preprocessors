import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.core.http.HttpMethod

fun BasePathController1.asRouter(vertx: Vertx): Router {
    val router = Router.router(vertx)
    router.route("/base/get")
        .method(HttpMethod("GET"))
        .handler(this::get)
    router.route("/base")
        .method(HttpMethod("POST"))
        .handler(this::post)
    router.route("/base")
        .method(HttpMethod("PUT"))
        .handler(this::put)
    return router
}