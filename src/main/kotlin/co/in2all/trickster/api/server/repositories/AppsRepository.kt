package co.in2all.trickster.api.server.repositories

import co.in2all.trickster.api.server.entities.App
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.data.repository.query.Param

interface AppsRepository : GraphRepository<App> {
    @Query("MATCH (a:App) RETURN a")
    fun getAll(): List<App>

    @Query("MATCH (a:App) WHERE a.client_id = {clientId} RETURN a")
    fun getByClientId(@Param("clientId") clientId: String): List<App>
}
