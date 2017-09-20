package io.in2all.tricksterapiserver.verticles

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future

class Application : AbstractVerticle() {
    override fun start(startFuture: Future<Void>?) {
        vertx.deployVerticle(AuthVerticle())
    }
}