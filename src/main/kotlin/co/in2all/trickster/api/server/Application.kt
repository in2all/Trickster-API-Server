package co.in2all.trickster.api.server

import co.in2all.trickster.api.server.routers.AuthRouter
import co.in2all.trickster.api.server.routers.UsersRouter
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import java.util.Date

object Application {
    val vertx = Vertx.vertx()!!

    private val router = Router.router(vertx)
    private val port = Integer.getInteger("http.port", 8080)

    fun start() {
        listOf(AuthRouter, UsersRouter)
                .forEach { router.mountSubRouter(it.route, it.router) }

        vertx
                .createHttpServer()
                .requestHandler { router.accept(it) }
                .listen(port) {
                    if (it.succeeded()) {
                        println("${Date()}: Server started on port $port.")
                    } else {
                        println(it.cause())
                    }
                }
    }
}