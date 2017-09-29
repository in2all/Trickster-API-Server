package co.in2all.trickster.api.server.entities

import org.neo4j.ogm.annotation.GraphId

data class AccessToken(
        @GraphId
        var id: Long? = null,

        var token: String? = null
)
