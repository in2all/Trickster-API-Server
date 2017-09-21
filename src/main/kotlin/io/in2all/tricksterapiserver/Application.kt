package io.in2all.tricksterapiserver

import io.in2all.tricksterapiserver.routers.AuthRouter
import io.in2all.tricksterapiserver.routers.UsersRouter
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.ext.web.Router

class Application : AbstractVerticle() {
    private val router = Router.router(vertx)

    override fun start(startFuture: Future<Void>?) {
        router.mountSubRouter(AuthRouter.route, AuthRouter.router)
        router.mountSubRouter(UsersRouter.route, UsersRouter.router)

        vertx
                .createHttpServer()
                .requestHandler { router.accept(it) }
                .listen(Integer.getInteger("http.port", 8080)) {
                    if (it.succeeded()) {
                        startFuture?.complete()
                    } else {
                        startFuture?.fail(it.cause())
                    }
                }
    }
}
