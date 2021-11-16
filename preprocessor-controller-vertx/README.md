# Preprocessor controller vertx

Code generation tool for [vertx (version 4+)](https://vertx.io/) spring style controllers with **ZERO** new runtime dependencies for _Kotlin_  
Tool uses **[Google KSP](https://github.com/google/ksp)** version 1.5.31-1.0.0

[Example project](../examples/vertx-controllers)

## Getting started:  
`build.gradle.kts`
```kotlin
plugins {  
    // or whatever kotlin you want
    kotlin("jvm") version "1.5.31"
    // google ksp
    id("com.google.devtools.ksp") version "1.5.31-1.0.0"
}
```

```kotlin
dependencies {
    // enable tool with ksp
    ksp("org.decembrist:preprocessor-controller-vertx:1.0.1")
    // annotations
    implementation("org.decembrist:preprocessor-controller-vertx:1.0.1")
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

Tested kotlin version 1.5.31  
Tested vertx version 4.1.5  

### Possible annotations:  
Mark top level class as controller  
_@Controller(*parent path)_ 

For multiple methods you could use REQUEST annotation  
_@REQUEST([methods], _path_)_

### All method annotations are repeatable  
_@ALL(path)_  
_@OPTIONS(path)_  
_@GET(path)_  
_@HEAD(path)_  
_@POST(path)_  
_@PUT(path)_  
_@DELETE(path)_  
_@TRACE(path)_  
_@CONNECT(path)_  
_@PATCH(path)_  
_@PROPFIND(path)_  
_@PROPPATCH(path)_  
_@MKCOL(path)_  
_@COPY(path)_  
_@MOVE(path)_  
_@LOCK(path)_  
_@UNLOCK(path)_  
_@MKCALENDAR(path)_  
_@VERSION_CONTROL(path)_  
_@REPORT(path)_  
_@CHECKIN(path)_  
_@CHECKOUT(path)_  
_@UNCHECKOUT(path)_  
_@MKWORKSPACE(path)_  
_@UPDATE(path)_  
_@LABEL(path)_  
_@MERGE(path)_  
_@BASELINE_CONTROL(path)_  
_@MKACTIVITY(path)_  
_@ORDERPATCH(path)_  
_@ACL(path)_  
_@SEARCH(path)_  