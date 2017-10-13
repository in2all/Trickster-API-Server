package co.in2all.trickster.api.server.repository

import co.in2all.trickster.api.server.entity.User
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.data.repository.query.Param

interface UsersRepository : GraphRepository<User>{
    @Query("MATCH (u:User {email: {email}}) RETURN u")
    fun get(@Param("email") email: String): User?

    @Query("MATCH (u:User {email: {email}, password: {password}}) RETURN u")
    fun get(@Param("email") email: String,
            @Param("password") password: String): User?

    @Query("CREATE (:User {email: {email}, password: {password}})")
    fun create(@Param("email") email: String,
               @Param("password") password: String)

    @Query("MATCH (u:User {email: {email}}) " +
           "SET u.password = {new_password}")
    fun changePassword(
            @Param("email") email: String,
            @Param("new_password") newPassword: String)
}
