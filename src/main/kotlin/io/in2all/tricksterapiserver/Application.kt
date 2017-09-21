package io.in2all.tricksterapiserver

import io.in2all.tricksterapiserver.routers.AuthRouter
import io.in2all.tricksterapiserver.routers.UsersRouter
import io.vertx.core.Vertx
import io.vertx.ext.web.Router

object Application {
    val vertx = Vertx.vertx()!!
    private val router = Router.router(vertx)

    fun start() {
        router.mountSubRouter(AuthRouter.route, AuthRouter.router)
        router.mountSubRouter(UsersRouter.route, UsersRouter.router)

        vertx
                .createHttpServer()
                .requestHandler { router.accept(it) }
                .listen(Integer.getInteger("http.port", 8080)) {
                    if (it.succeeded()) {
                        println("Server has started.")
                    } else {
                        println(it.cause())
                    }
                }
    }
}