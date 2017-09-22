package co.in2all.trickster.api.server

import co.in2all.trickster.api.server.routers.AuthRouter
import co.in2all.trickster.api.server.routers.UsersRouter
import io.vertx.core.Vertx
import io.vertx.ext.web.Router

object Application {
    val vertx = Vertx.vertx()!!
    private val router = Router.router(vertx)

    fun start() {
        listOf(AuthRouter, UsersRouter)
                .forEach { router.mountSubRouter(it.route, it.router) }

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