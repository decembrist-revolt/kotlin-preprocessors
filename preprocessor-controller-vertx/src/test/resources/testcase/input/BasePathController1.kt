import io.vertx.ext.web.RoutingContext
import org.decembrist.vertx.annotations.*
import org.decembrist.vertx.annotations.Controller

@Controller("base")
class BasePathController1 {
    @GET("/get")
    fun get(context: RoutingContext) {
    }

    @POST
    fun post(ctx: RoutingContext) {
    }

    @PUT("/")
    fun put(ctx: RoutingContext) {
    }
}