package io.in2all.tricksterapiserver

import io.in2all.tricksterapiserver.verticles.Application
import io.vertx.core.Launcher

fun main(args: Array<String>) {
    Launcher.executeCommand("run", Application::class.java.name)
}