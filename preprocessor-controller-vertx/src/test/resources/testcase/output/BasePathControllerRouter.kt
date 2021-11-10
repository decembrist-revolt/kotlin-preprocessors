import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.core.http.HttpMethod

fun BasePathController.asRouter(vertx: Vertx): Router {
    val router = Router.router(vertx)
    router.route("/base/get")
        .method(HttpMethod("GET"))
        .handler(this::get)
    return router
}