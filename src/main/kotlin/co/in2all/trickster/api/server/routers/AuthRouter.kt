package co.in2all.trickster.api.server.routers

import co.in2all.trickster.api.server.Application
import io.vertx.ext.web.Router

object AuthRouter : IRouter {
    override val route = "/"
    override val router = Router
            .router(Application.vertx)
            .apply {
                get("/signin").handler { it.response().end("Sign in page") }
                get("/signup").handler { it.response().end("Sign up page") }
            }!!
}
