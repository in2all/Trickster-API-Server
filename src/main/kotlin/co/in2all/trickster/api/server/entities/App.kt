package co.in2all.trickster.api.server.entities

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity

@NodeEntity
data class App (
    @GraphId
    var id: Long? = null,

    var client_id: String? = null,
    var client_secret: String? = null,
    var name: String? = null,
    var redirect_uri: String? = null

//    @Relationship(type = "STARTED_IN", direction = Relationship.INCOMING)
)
