package co.in2all.trickster.api.server.utils

import java.util.UUID

object Safeguard {
    fun getUniqueToken() = UUID.randomUUID().toString().split("-").joinToString("")
}
