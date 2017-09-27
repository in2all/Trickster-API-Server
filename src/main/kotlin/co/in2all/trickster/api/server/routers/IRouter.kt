package co.in2all.trickster.api.server.routers

import io.vertx.ext.web.Router

interface IRouter {
    val route: String
    val router: Router
}
