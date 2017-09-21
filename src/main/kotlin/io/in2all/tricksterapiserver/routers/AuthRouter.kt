package io.in2all.tricksterapiserver.routers

import io.vertx.core.Vertx
import io.vertx.ext.web.Router

object AuthRouter {
    val route = "/"
    val router = Router
            .router(Vertx.vertx())
            .apply {
                get("/signin").handler { it.response().end("Sign in page") }
                get("/signup").handler { it.response().end("Sign up page") }
            }!!
}
