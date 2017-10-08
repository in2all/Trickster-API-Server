package co.in2all.trickster.api.server.repository

import co.in2all.trickster.api.server.entity.App
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.data.repository.query.Param

interface AppsRepository : GraphRepository<App> {
    @Query("MATCH (a:App {client_id: {client_id}}) RETURN a")
    fun get(@Param("client_id") clientId: String): App?
}
