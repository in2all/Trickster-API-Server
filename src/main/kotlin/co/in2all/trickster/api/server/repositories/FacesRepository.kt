package co.in2all.trickster.api.server.repositories

import co.in2all.trickster.api.server.entities.Face
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.data.repository.query.Param

interface FacesRepository : GraphRepository<Face> {
    @Query("MATCH (:Session {access_token: {access_token}})-[:STARTED_BY]->(:User)<-[:CREATED_BY]-(f:Face) " +
           "RETURN f")
    fun getAll(@Param("access_token") accessToken: String): List<Face>

//    @Query("MATCH (f:Face) RETURN f")
//    fun getAll(): List<Face>

    @Query("MATCH (f:Face { name: {name} }) RETURN f")
    fun get(@Param("name") name: String): Face?

//    @Query("MATCH (f:Face {name: {name}})-[:CREATED_BY]->(:User)<-[:STARTED_BY]-(:Session {access_token: {access_token}}) " +
//           "RETURN f")
//    fun get(@Param("name") name: String,
//            @Param("access_token") accessToken: String): Face?

    @Query("MATCH (:Session {access_token: {access_token}})-[:STARTED_BY]->(:User)<-[:CREATED_BY]-(f:Face) " +
            "RETURN count(f)")
    fun count(@Param("access_token") accessToken: String): Long

    @Query("MATCH (:Session { access_token: {access_token} })-[:STARTED_BY]->(u:User) " +
           "CREATE (:Face { name: {name}, description: {description}, avatar: {avatar}})-[:CREATED_BY]->(u)")
    fun create(@Param("access_token") accessToken: String,
               @Param("name") name: String,
               @Param("description") description: String,
               @Param("avatar") avatar: String)

//    @Query("MATCH (f:Face {name: {name}})-[:CREATED_BY]->(:User)<-[:STARTED_BY]-(:Session {access_token: {access_token}}) " +
//           "DETACH DELETE f")
//    fun delete(@Param("name") name: String,
//               @Param("access_token") accessToken: String)
}