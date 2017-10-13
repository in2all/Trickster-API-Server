package co.in2all.trickster.api.server.service

import co.in2all.trickster.api.server.entity.User

interface ChangePasswordService {
    fun createToken(email: String, clientId: String?, token: String)
    fun changePassword(user: User, newPassword: String)
}