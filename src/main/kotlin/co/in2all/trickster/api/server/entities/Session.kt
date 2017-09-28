package co.in2all.trickster.api.server.entities

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship

@NodeEntity
data class Session (
    @GraphId
    var id: Long? = null,

    var access_token: String? = null,
    var auth_token: String? = null,
    var expires_in: Int? = null,

    @Relationship(type = "STARTED_IN", direction = Relationship.OUTGOING)
    var app: App? = null
)