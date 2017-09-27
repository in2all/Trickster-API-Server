package co.in2all.trickster.api.server.routers

import co.in2all.trickster.api.server.Application
import io.vertx.ext.web.Router

object ErrorsRouter : IRouter {
    override val route: String = "/"
    override val router: Router = Router
            .router(Application.vertx)
            .apply {
                get("/*").handler { it.response().end("404") }
            }
}
