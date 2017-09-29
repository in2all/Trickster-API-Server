package co.in2all.trickster.api.server.repositories

import co.in2all.trickster.api.server.entities.AccessToken
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.data.repository.query.Param

interface AccessTokensRepository : GraphRepository<AccessToken> {
    @Query("MATCH (t:AccessToken {token: {token}})-[:REQUESTED_FOR]->(:App {client_id: {client_id}, client_secret: {client_secret}}) " +
           "RETURN t")
    fun get(@Param("token") token: String,
            @Param("client_id") clientId: String,
            @Param("client_secret") clientSecret: String): AccessToken?

    @Query("MATCH (u:User {email: {email}}), (a:App {client_id: {client_id}}) " +
           "CREATE (t:AccessToken {token: {token}})-[:REQUESTED_BY]->(u), (t)-[:REQUESTED_FOR]->(a)")
    fun create(@Param("email") email: String,
               @Param("client_id") clientId: String,
               @Param("token") token: String)
}
