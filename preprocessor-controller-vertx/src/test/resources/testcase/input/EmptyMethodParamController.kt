package testcase.input

import org.decembrist.vertx.annotations.Controller
import org.decembrist.vertx.annotations.GET

@Controller
class EmptyMethodParamController {
    @GET("/path")
    fun wrong() {
    }
}