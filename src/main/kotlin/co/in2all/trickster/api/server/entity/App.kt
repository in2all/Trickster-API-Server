package co.in2all.trickster.api.server.entity

import org.neo4j.ogm.annotation.GraphId
import org.neo4j.ogm.annotation.NodeEntity

@NodeEntity
data class App(
        @GraphId
        private var id: Long? = null,

        var name: String? = null,
        var client_id: String? = null,
        var client_secret: String? = null,
        var redirect_uri: String? = null
)
