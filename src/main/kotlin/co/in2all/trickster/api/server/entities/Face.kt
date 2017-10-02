package co.in2all.trickster.api.server.entities

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity

@NodeEntity
data class Face(
        @GraphId
        private var id: Long? = null,

        var name: String? = null,
        var description: String? = null,
        var avatar: String? = null
)