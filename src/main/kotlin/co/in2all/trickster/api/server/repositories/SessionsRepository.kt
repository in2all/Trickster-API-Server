package co.in2all.trickster.api.server.repositories

import co.in2all.trickster.api.server.entities.Session
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.data.repository.query.Param

interface SessionsRepository : GraphRepository<Session> {
    @Query("MATCH (s:Session {access_token: {access_token}}) RETURN s")
    fun get(@Param("access_token") accessToken: String): Session?

    @Query("MATCH (s:Session { refresh_token: {refresh_token} })-[:STARTED_IN]->(a:App { client_id: {client_id}, client_secret: {client_secret} }) " +
           "RETURN s")
    fun get(@Param("refresh_token") refreshToken: String,
            @Param("client_id") clientId: String,
            @Param("client_secret") clientSecret: String): Session?

    @Query("MATCH (t:AuthToken {token: {token}})-[:REQUESTED_BY]->(u), " +
           "      (t)-[:REQUESTED_FOR]->(a) " +
           "DETACH DELETE t " +
           "CREATE (s:Session {access_token: {access_token}, expires_in: {expires_in}, refresh_token: {refresh_token}})-[:STARTED_BY]->(u), " +
           "       (s)-[:STARTED_IN]->(a)")
    fun create(@Param("token") authToken: String,
               @Param("access_token") accessToken: String,
               @Param("expires_in") expiresIn: Long,
               @Param("refresh_token") refreshToken: String)

    @Query("MATCH (s:Session { refresh_token: {refresh_token} })-[:STARTED_BY]->(u:User), " +
           "      (s)-[:STARTED_IN]->(a:App) " +
           "DETACH DELETE s " +
           "CREATE (rs:Session { access_token: {new_access_token}, expires_in: {new_expires_in}, refresh_token: {new_refresh_token} })-[:STARTED_BY]->(u), " +
           "       (rs)-[:STARTED_IN]->(a) " +
           "RETURN rs")
    fun refresh(@Param("refresh_token") refreshToken: String,
                @Param("new_access_token") newAccessToken: String,
                @Param("new_expires_in") newExpiresIn: Long,
                @Param("new_refresh_token") newRefreshToken: String): Session
}
