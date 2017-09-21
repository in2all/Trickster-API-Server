package io.in2all.tricksterapiserver

import io.vertx.core.Launcher

fun main(args: Array<String>) {
    Launcher.executeCommand("run", Application::class.java.name)
}
