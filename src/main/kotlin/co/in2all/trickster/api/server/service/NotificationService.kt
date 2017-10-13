package co.in2all.trickster.api.server.service

interface NotificationService {
    fun sendEmailConfirmMessage(email: String, confirmToken: String)
    fun sendPasswordRefreshMessage(email: String, refreshToken: String)
}
