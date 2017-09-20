package io.in2all.tricksterapiserver.verticles

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.ext.web.Router

class AuthVerticle : AbstractVerticle() {
    override fun start(startFuture: Future<Void>?) {
        val router = Router
                .router(vertx)
                .apply {
                    get("/signin").handler { it.response().end("Sign in page") }
                    get("/signup").handler { it.response().end("Sign up page") }
                }

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