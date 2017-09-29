package co.in2all.trickster.api.server.repositories

import co.in2all.trickster.api.server.entities.Session
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.data.repository.query.Param

interface SessionsRepository : GraphRepository<Session> {
    @Query("MATCH (s:Session {auth_token: {auth_token}}) RETURN s")
    fun get(@Param("auth_token") authToken: String): Session?

    @Query("MATCH (t:AccessToken {token: {token}})-[:REQUESTED_BY]->(u), " +
           "      (t)-[:REQUESTED_FOR]->(a) " +
           "DETACH DELETE t " +
           "CREATE (s:Session {auth_token: {auth_token}, expires_in: {expires_in}, refresh_token: {refresh_token}})-[:STARTED_BY]->(u), " +
           "       (s)-[:STARTED_IN]->(a)")
    fun create(@Param("token") accessToken: String,
               @Param("auth_token") authToken: String,
               @Param("expires_in") expiresIn: Long,
               @Param("refresh_token") refreshToken: String)
}
