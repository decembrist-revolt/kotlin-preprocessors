import io.vertx.ext.web.RoutingContext
import org.decembrist.vertx.annotations.*
import org.decembrist.vertx.annotations.Controller

@Controller("base")
class BasePathController {
    @GET("/get")
    fun get(context: RoutingContext) {
    }
}