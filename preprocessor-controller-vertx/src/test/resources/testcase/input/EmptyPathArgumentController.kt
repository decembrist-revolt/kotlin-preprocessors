package testcase.input

import org.decembrist.vertx.annotations.Controller
import org.decembrist.vertx.annotations.GET

@Controller
class EmptyPathArgumentController {
    @GET("")
    fun emptyPath(wrong: String) {
    }
}