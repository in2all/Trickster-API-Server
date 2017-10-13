package co.in2all.trickster.api.server.repository

import co.in2all.trickster.api.server.entity.RefreshPasswordToken
import co.in2all.trickster.api.server.entity.User
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.data.repository.query.Param

interface RefreshPasswordTokensRepository : GraphRepository<RefreshPasswordToken> {

    @Query("MATCH (rpt:RefreshPasswordToken)-[:REQUESTED_BY]->(:User {email: {email}}) " +
           "RETURN rpt")
    fun getByEmail(@Param("email") email: String): RefreshPasswordToken?

    @Query("MATCH (rpt:RefreshPasswordToken {token: {token}}) " +
           "RETURN rpt")
    fun getByToken(@Param("token") token: String): RefreshPasswordToken?

    @Query("MATCH (:RefreshPasswordToken {token: {token}})-[:REQUESTED_BY]->(u:User) " +
           "RETURN u")
    fun getUser(@Param("token") token: String): User?

    @Query("MATCH (u:User {email: {email}}) " +
           "CREATE (:RefreshPasswordToken {token: {token}, client_id: {client_id}, expires_in: {expires_in}})-[:REQUESTED_BY]->(u)")
    fun create(@Param("email") email: String,
               @Param("token") token: String,
               @Param("client_id") clientId: String?,
               @Param("expires_in") expiresIn: Long)

    @Query("MATCH (rpt:RefreshPasswordToken)-[:REQUESTED_BY]->(:User {email: {email}}) " +
           "SET rpt.token = {new_token}, rpt.client_id = {new_client_id}, rpt.expires_in = {new_expires_in}")
    fun refresh(@Param("email") email: String,
                @Param("new_token") newToken: String,
                @Param("new_client_id") newClientId: String?,
                @Param("new_expires_in") newExpiresIn: Long)

    @Query("MATCH (rpt:RefreshPasswordToken {token: {token}}) " +
           "DETACH DELETE rpt")
    fun delete(@Param("token") token: String)
}
