package co.in2all.trickster.api.server.service

import co.in2all.trickster.api.server.entity.User

interface NotificationService {
    fun sendEmailConfirmMessage(user: User, confirmToken: String)
}