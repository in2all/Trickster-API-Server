package io.in2all.tricksterapiserver.routers

import io.in2all.tricksterapiserver.models.User
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import io.vertx.ext.web.Router

object UsersRouter {
    val route = "/users"
    val router = Router
            .router(Vertx.vertx())
            .apply {
                get("/:id").handler {
                    val id = it.pathParams()["id"]

                    it.response().putHeader("Content-Type", "application/json; charset=utf-8")
                    it.response().end(Json.encodePrettily(User(id?.toInt())))
                }
            }
}
