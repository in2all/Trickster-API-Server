package co.in2all.trickster.api.server.routers

import co.in2all.trickster.api.server.Application
import co.in2all.trickster.api.server.models.User
import io.vertx.core.json.Json
import io.vertx.ext.web.Router

object UsersRouter : IRouter {
    override val route = "/users"
    override val router: Router = Router
            .router(Application.vertx)
            .apply {
                get("/:id").handler {
                    val id = it.pathParams()["id"]

                    it.response().putHeader("Content-Type", "application/json; charset=utf-8")
                    it.response().end(Json.encodePrettily(User(id?.toInt())))
                }
            }
}
