package co.in2all.trickster.api.server.entity

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity

@NodeEntity
data class Message(
        @GraphId
        var id: Long? = null,

        var text: String? = null,
        var popularity_index: Int? = null,
        var created: String? = null
)