package testcase.input

import io.vertx.ext.web.RoutingContext
import org.decembrist.vertx.annotations.Controller
import org.decembrist.vertx.annotations.GET

@Controller
class MultipleMethodParamController {
    @GET("/path")
    fun wrong(ctx: RoutingContext, wrong1: String) {
    }
}