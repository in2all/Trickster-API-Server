package co.in2all.trickster.api.server.entity

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity

@NodeEntity
data class Board(
        @GraphId
        var id: Long? = null,

        var name: String? = null,
        var route: String? = null,
        var description: String? = null,
        var avatar: String? = null,
        var popularity_index: Int? = null,

        private var created: Long? = null
)
