package co.in2all.trickster.api.server.repository

import co.in2all.trickster.api.server.entity.AuthToken
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.data.repository.query.Param

interface AuthTokensRepository : GraphRepository<AuthToken> {
    @Query("MATCH (t:AuthToken {token: {token}})-[:REQUESTED_FOR]->(:App {client_id: {client_id}, client_secret: {client_secret}}) " +
           "RETURN t")
    fun get(@Param("token") token: String,
            @Param("client_id") clientId: String,
            @Param("client_secret") clientSecret: String): AuthToken?

    @Query("MATCH (u:User {email: {email}}), (a:App {client_id: {client_id}}) " +
           "CREATE (t:AuthToken {token: {token}, expires_in: {expires_in}})-[:REQUESTED_BY]->(u)," +
           "       (t)-[:REQUESTED_FOR]->(a)")
    fun create(@Param("email") email: String,
               @Param("client_id") clientId: String,
               @Param("token") token: String,
               @Param("expires_in") expiresIn: Long)
}
