package co.in2all.trickster.api.server

import co.in2all.trickster.api.server.entity.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.MailException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class NotificationService @Autowired constructor(
        val javaMailSender: JavaMailSender) {

    @Throws(MailException::class)
    fun sendNotification(user: User) {
        val mail = SimpleMailMessage()
        mail.setTo(user.email)
        mail.subject = "Test email."
        mail.from = "service@trickster.im"
        mail.text = "Everything is ok!"

        javaMailSender.send(mail)
    }
}