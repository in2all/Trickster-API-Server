package co.in2all.trickster.api.server.entities

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity

@NodeEntity
data class Session (
    @GraphId
    var id: Long? = null,

    var access_token: String? = null,
    var auth_token: String? = null,
    var expires_in: Long? = null

//    @Relationship(type = "STARTED_IN", direction = Relationship.OUTGOING)
//    var app: App? = null
)
