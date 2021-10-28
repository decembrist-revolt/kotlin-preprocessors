import io.vertx.ext.web.RoutingContext
import org.decembrist.vertx.annotations.*
import org.decembrist.vertx.annotations.Controller

@Controller
class Controller1 {
    @GET("/get")
    fun get(context: RoutingContext) {
    }

    @POST("/post")
    fun post(context: RoutingContext) {
    }

    @REQUEST(method = ["POST", "GET"], path = "/request")
    fun request(context: RoutingContext) {
    }

    @DELETE("/delete")
    @PUT("/put")
    fun paths(context: RoutingContext) {
    }

    @GET("/firstGetRepeatable")
    @GET("/secondGetRepeatable")
    fun repeatableMethod(context: RoutingContext) {
    }

    @REQUEST(method = ["GET", "POST"], "/getPostRepeatableRequest")
    @REQUEST(method = ["PUT", "HEAD"], "/putHeadRepeatableRequest")
    fun repeatableRequest(context: RoutingContext) {
    }
}