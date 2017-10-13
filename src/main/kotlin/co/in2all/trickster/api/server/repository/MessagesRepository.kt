package co.in2all.trickster.api.server.repository

import co.in2all.trickster.api.server.entity.Message
import org.springframework.data.neo4j.repository.GraphRepository

interface MessagesRepository : GraphRepository<Message> {}
