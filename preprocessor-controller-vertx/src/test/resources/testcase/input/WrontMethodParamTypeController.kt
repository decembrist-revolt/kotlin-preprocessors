package testcase.input

import org.decembrist.vertx.annotations.Controller
import org.decembrist.vertx.annotations.GET

@Controller
class WrontMethodParamTypeController {
    @GET("/path")
    fun wrong(wrong: String) {
    }
}