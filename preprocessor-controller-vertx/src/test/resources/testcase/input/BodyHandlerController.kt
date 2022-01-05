import io.vertx.ext.web.RoutingContext
import org.decembrist.vertx.annotations.BodyHandler
import org.decembrist.vertx.annotations.Controller
import org.decembrist.vertx.annotations.GET
import org.decembrist.vertx.annotations.POST
import org.decembrist.vertx.annotations.REQUEST

@Controller
class BodyHandlerController {
    @GET("/get")
    fun getWithoutBH(context: RoutingContext) {
    }

    @POST
    fun postWithoutBH(ctx: RoutingContext) {
    }

    @REQUEST(method = ["PUT", "OPTIONS"], "/")
    fun requestWithoutBH(ctx: RoutingContext) {
    }

    @GET("/get", bodyHandler = BodyHandler(true))
    fun getWithBH(context: RoutingContext) {
    }

    @POST(bodyHandler = BodyHandler(true))
    fun postWithBH(ctx: RoutingContext) {
    }

    @REQUEST(method = ["PUT", "OPTIONS"], "/", bodyHandler = BodyHandler(true))
    fun requestWithBH(ctx: RoutingContext) {
    }
}