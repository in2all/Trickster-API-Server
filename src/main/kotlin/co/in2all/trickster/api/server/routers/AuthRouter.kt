@file:Suppress("DEPRECATION")

package co.in2all.trickster.api.server.routers

import co.in2all.trickster.api.server.Application
import io.vertx.ext.web.Router
import io.vertx.ext.web.templ.FreeMarkerTemplateEngine

object AuthRouter : IRouter {
    override val route = "/"
    override val router: Router = Router
            .router(Application.vertx)
            .apply {
                get("/signin/:client_id").handler { ctx ->
                    engine.render(
                            ctx,
                            "src/main/templates/signin.ftl") { res ->
                        if (res.succeeded()) {
                            ctx.response().end(res.result())
                        } else {
                            ctx.fail(res.cause())
                        }
                    }
                }

                post("/signin/:appId").handler { req ->
                    Application.dbClient.getConnection { conn ->
                        if (conn.failed()) {
                            // TODO: Add 500 error page rendering.
                            req.response().setStatusCode(500).end("500")
                        } else {
                            conn
                                    .result()
                                    .query("match (n:User {name: \"${req.pathParams()["appId"]}\"}) return n") { res ->
                                        if (res.failed()) {
                                            // TODO: Add 500 error page rendering.
                                            req.response().setStatusCode(500).end("500")
                                        } else {
                                            if (res.result() != null) {
                                                req.response().end(res.result().results[0].toString())
                                            } else {
                                                // TODO: Add error message.
                                                req.response().end("Error")
                                            }
                                        }
                                    }
                        }
                    }
                }

                get("/signup").handler { it.response().end("Sign up page") }
                get("/auth").handler { it.response().end("Auth page") }
            }

    private val engine = FreeMarkerTemplateEngine.create()
}
