package co.in2all.trickster.api.server.repositories

import co.in2all.trickster.api.server.entities.Board
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.data.repository.query.Param

interface BoardsRepository : GraphRepository<Board> {
    @Query("MATCH (b: Board) WHERE ID(b) = {id} RETURN b")
    fun get(@Param("id") id: Long): Board?
}
