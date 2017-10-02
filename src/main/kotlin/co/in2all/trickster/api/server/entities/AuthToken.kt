package co.in2all.trickster.api.server.entities

import org.neo4j.ogm.annotation.GraphId

data class AuthToken(
        @GraphId
        private var id: Long? = null,

        var token: String? = null,
        var expires_in: Long? = null
)
