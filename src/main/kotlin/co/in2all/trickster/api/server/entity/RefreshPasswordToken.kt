package co.in2all.trickster.api.server.entity

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship

@NodeEntity
data class RefreshPasswordToken(
        @GraphId
        private var id: Long? = null,

        var token: String? = null,
        var client_id: String? = null,
        var expires_in: Long? = null,

        @Relationship(type = "REQUESTED_BY", direction = Relationship.OUTGOING)
        var user: User? = null
)
