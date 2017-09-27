package co.in2all.trickster.api.server

import co.in2all.trickster.api.server.routers.AuthRouter
import co.in2all.trickster.api.server.routers.ErrorsRouter
import co.in2all.trickster.api.server.routers.UsersRouter
import io.vertx.core.Vertx
import io.vertx.ext.jdbc.JDBCClient
import io.vertx.ext.web.Router
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import java.util.*

object Application {
    val vertx: Vertx = Vertx.vertx()
    val dbClient: JDBCClient = JDBCClient.createShared(vertx, json {
        obj(
                "url" to "jdbc:neo4j:bolt://localhost/",
                "driver_class" to org.neo4j.jdbc.Driver::class.java.name,
                "max_pool_size" to 100,
                "user" to "neo4j",
                "password" to "samplepassword"
        )
    })

    private val router = Router.router(vertx)
    private val port = Integer.getInteger("http.port", 8080)

    fun start() {
        listOf(AuthRouter, UsersRouter, ErrorsRouter)
                .forEach { router.mountSubRouter(it.route, it.router) }

        // TODO: Удалить нахуй после того, как разберемся с доступом к БД.
        dbClient.getConnection { conn ->
            if (conn.failed()) {
                println(conn.cause())
            } else {
                println("Vse norm ;)")
            }

            val connection = conn.result()
            connection.query("MATCH (n) return n") { res ->
                if (res.failed()) {
                    println(res.cause())
                } else {
//                    res
//                            .result()
//                            .results
//                            .forEach { println(it) }
                    println(res.result().toJson())
                }
            }
        }

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
