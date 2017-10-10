package co.in2all.trickster.api.server.repository

import co.in2all.trickster.api.server.entity.SignupData
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.data.repository.query.Param

interface SignupDataRepository : GraphRepository<SignupData> {
    @Query("MATCH (sd:SignupData {token: {token}}) RETURN sd")
    fun get(@Param("token") token: String): SignupData?

    @Query("CREATE (:SignupData {email: {email}, password: {password}, client_id: {client_id}, token: {token}, expires_in: {expires_in}})")
    fun create(@Param("email") email: String,
               @Param("password") password: String,
               @Param("client_id") clientId: String,
               @Param("token") token: String,
               @Param("expires_in") expiresIn: Long)

    @Query("MATCH (sd:SignupData {token: {token}}) DELETE sd")
    fun delete(@Param("token") token: String)
}
