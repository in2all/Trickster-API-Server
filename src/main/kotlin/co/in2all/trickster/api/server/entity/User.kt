package co.in2all.trickster.api.server.entity

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity

@NodeEntity
data class User(
        @GraphId
        private var id: Long? = null,

        var email: String? = null,
        var password: String? = null
)
