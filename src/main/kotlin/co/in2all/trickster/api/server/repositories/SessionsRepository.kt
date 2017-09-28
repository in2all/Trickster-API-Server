package co.in2all.trickster.api.server.repositories

import co.in2all.trickster.api.server.entities.Session
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.data.repository.query.Param

interface SessionsRepository : GraphRepository<Session> {
    @Query("MATCH (u:User {email: {email}}) " +
           "MATCH (a:App {client_id: {clientId}}) " +
           "CREATE (s:Session {access_token: {accessToken}, auth_token: {authToken}, expires_in: {expiresIn}})-[:STARTED_BY]->(u), (s)-[:STARTED_IN]->(a)")
    fun createSession(
            @Param("email") email: String,
            @Param("clientId") clientId: String,
            @Param("accessToken") accessToken: String,
            @Param("authToken") authToken: String,
            @Param("expiresIn") expiresIn: Long)
}
