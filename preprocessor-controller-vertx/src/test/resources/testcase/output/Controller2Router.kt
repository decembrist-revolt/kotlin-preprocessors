package testcase.input

import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.core.http.HttpMethod

fun testcase.input.Controller2.asRouter(vertx: Vertx): Router {
    val router = Router.router(vertx)
    router.route("/get")
        .method(HttpMethod("GET"))
        .handler(this::get)
    router.route("/post")
        .method(HttpMethod("POST"))
        .handler(this::post)
    router.route("/request")
        .method(HttpMethod("POST"))
        .method(HttpMethod("GET"))
        .handler(this::request)
    router.route("/put")
        .method(HttpMethod("PUT"))
        .handler(this::paths)
    router.route("/delete")
        .method(HttpMethod("DELETE"))
        .handler(this::paths)
    router.route("/firstGetRepeatable")
        .method(HttpMethod("GET"))
        .handler(this::repeatableMethod)
    router.route("/secondGetRepeatable")
        .method(HttpMethod("GET"))
        .handler(this::repeatableMethod)
    router.route("/getPostRepeatableRequest")
        .method(HttpMethod("GET"))
        .method(HttpMethod("POST"))
        .handler(this::repeatableRequest)
    router.route("/putHeadRepeatableRequest")
        .method(HttpMethod("PUT"))
        .method(HttpMethod("HEAD"))
        .handler(this::repeatableRequest)
    return router
}