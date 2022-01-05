# Preprocessor controller vertx

Code generation tool for [vertx (version 4+)](https://vertx.io/) spring style controllers with **ZERO** new runtime dependencies for _Kotlin_  
Tool uses **[Google KSP](https://github.com/google/ksp)** version 1.6.10-1.0.2

[Example project](../examples/vertx-controllers)

## Getting started:  
`build.gradle.kts`
```kotlin
plugins {  
    // or whatever kotlin you want
    kotlin("jvm") version "1.6.10"
    // google ksp
    id("com.google.devtools.ksp") version "1.6.10-1.0.2"
}
```

```kotlin
dependencies {
    // enable tool with ksp
    ksp("org.decembrist:preprocessor-controller-vertx:1.0.3")
    // annotations
    compileOnly("org.decembrist:preprocessor-controller-vertx:1.0.3")
    ...
    // add vertx web dependency to use
}
```

```kotlin
// include generated sources for compile
kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}
```
`Main.kt`
```kotlin
import io.vertx.ext.web.RoutingContext
import org.decembrist.vertx.annotations.Controller
import org.decembrist.vertx.annotations.GET
// mark top level class as controller (router creates for every controller)
// with base mapping '/parent' (could be empty)
@Controller("/parent")
class HelloController {
    // handle get request for path '/parent/hello'
    @GET("/hello")
    // method should have only one input param of type RoutingContext (with whatever return type)
    fun hello(ctx: RoutingContext) {
        ctx.response().send("Hello World")
    }
}
```
KSP will generate file `build/generated/ksp/main/kotlin/HelloControllerRouter.kt` on compile  
`HelloControllerRouter.kt`
```kotlin
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.core.http.HttpMethod

fun org.decembrist.HelloController.asRouter(vertx: Vertx): Router {
    val router = Router.router(vertx)
    router.route("/parent/hello")
        .method(HttpMethod("GET"))
        .handler(this::hello)
    return router
}
```
You can use router as any **Vertx** router  
```kotlin
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
```
Start application and GET Request for http://localhost:80/parent/hello returns _Hello World_  
Check [example project](../examples/vertx-controllers)

Tested kotlin version 1.6.10  
Tested vertx version 4.2.2

### Possible annotations:

Mark top level class as controller  
_@Controller(*parent path)_

For multiple methods you could use REQUEST annotation  
_@REQUEST([methods], _path_, _bodyHandler_)_

### Body handler

To enable BodyHandler for route you can use bodyHandler param

```kotlin
@GET("/hello", bodyHandler = BodyHandler(true))
fun hello(ctx: RoutingContext) {
    val bodyContent = ctx.bodyAsString
    ...
}
```

Generated code

```kotlin
router.route("/hello")
    .method(HttpMethod("GET"))
    .method(HttpMethod("POST"))
    .handler(BodyHandler.create())
    .handler(this::hello)
```

### All method annotations are repeatable

_@ALL(path, bodyHandler)_  
_@OPTIONS(path, bodyHandler)_  
_@GET(path, bodyHandler)_  
_@HEAD(path, bodyHandler)_  
_@POST(path, bodyHandler)_  
_@PUT(path, bodyHandler)_  
_@DELETE(path, bodyHandler)_  
_@TRACE(path, bodyHandler)_  
_@CONNECT(path, bodyHandler)_  
_@PATCH(path, bodyHandler)_  
_@PROPFIND(path, bodyHandler)_  
_@PROPPATCH(path, bodyHandler)_  
_@MKCOL(path, bodyHandler)_  
_@COPY(path, bodyHandler)_  
_@MOVE(path, bodyHandler)_  
_@LOCK(path, bodyHandler)_  
_@UNLOCK(path, bodyHandler)_  
_@MKCALENDAR(path, bodyHandler)_  
_@VERSION_CONTROL(path, bodyHandler)_  
_@REPORT(path, bodyHandler)_  
_@CHECKIN(path, bodyHandler)_  
_@CHECKOUT(path, bodyHandler)_  
_@UNCHECKOUT(path, bodyHandler)_  
_@MKWORKSPACE(path, bodyHandler)_  
_@UPDATE(path, bodyHandler)_  
_@LABEL(path, bodyHandler)_  
_@MERGE(path, bodyHandler)_  
_@BASELINE_CONTROL(path, bodyHandler)_  
_@MKACTIVITY(path, bodyHandler)_  
_@ORDERPATCH(path, bodyHandler)_  
_@ACL(path, bodyHandler)_  
_@SEARCH(path, bodyHandler)_  