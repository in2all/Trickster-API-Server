package co.in2all.trickster.api.server.repositories

import co.in2all.trickster.api.server.entities.User
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.data.repository.query.Param

interface UsersRepository : GraphRepository<User>{
    @Query("MATCH (u:User) " +
           "WHERE u.email = {email} and u.password = {password} " +
           "RETURN u")
    fun getByEmailAndPassword(
            @Param("email") email: String,
            @Param("password") password: String): User?
}
