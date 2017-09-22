@file:Suppress("DEPRECATION")

package co.in2all.trickster.api.server.routers

import co.in2all.trickster.api.server.Application
import io.vertx.ext.web.Router
import io.vertx.ext.web.templ.FreeMarkerTemplateEngine

object AuthRouter : IRouter {
    override val route = "/"
    override val router = Router
            .router(Application.vertx)
            .apply {
                get("/signin/:appId").handler { ctx ->
                    engine.render(
                            ctx,
                            "src/main/kotlin/co/in2all/trickster/api/server/templates/signin.ftl") { res ->
                        if (res.succeeded()) {
                            ctx.response().end(res.result())
                        } else {
                            ctx.fail(res.cause())
                        }
                    }
                }
                post("/signin/:appId").handler { it.response().end(it.pathParams()["appId"]) }
                get("/signup").handler { it.response().end("Sign up page") }
            }!!

    private val engine = FreeMarkerTemplateEngine.create()
}
