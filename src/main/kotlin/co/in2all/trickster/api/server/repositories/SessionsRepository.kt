package co.in2all.trickster.api.server.repositories

import co.in2all.trickster.api.server.entities.Session
import org.springframework.data.neo4j.repository.GraphRepository

interface SessionsRepository : GraphRepository<Session> {
}