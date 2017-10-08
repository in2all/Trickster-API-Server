package co.in2all.trickster.api.server.service

import co.in2all.trickster.api.server.entity.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.MailException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
internal class NotificationServiceImpl @Autowired constructor(
        val javaMailSender: JavaMailSender): NotificationService {

    @Throws(MailException::class)
    override fun sendEmailConfirmMessage(user: User, confirmToken: String) {
        val mail = SimpleMailMessage()
        mail.setTo(user.email)
        mail.subject = "Подтвердите регистрацию в Трикстере."
        mail.from = "service@trickster.im"
        mail.text = "Ваш почтовый ящик использован для регистрации.\n" +
                    "Если это сделали вы, перейдите по подтверждающей ссылке:\n" +
                    "https://api.trickster.im/confirm/$confirmToken\n\n" +
                    "В противном случае, просто проигнорируйте это письмо."

        javaMailSender.send(mail)
    }
}