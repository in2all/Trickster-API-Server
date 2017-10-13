package co.in2all.trickster.api.server.service.impl

import co.in2all.trickster.api.server.service.NotificationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.MailException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
internal class NotificationServiceImpl @Autowired constructor(
        val javaMailSender: JavaMailSender) : NotificationService {

    @Throws(MailException::class)
    override fun sendEmailConfirmMessage(email: String, confirmToken: String) {
        val mail = generateMail("Подтвердите регистрацию в Трикстере.",
                "Ваш почтовый ящик использован для регистрации.\n" +
                "Если это сделали вы, перейдите по подтверждающей ссылке:\n" +
                "https://api.trickster.im/confirm/$confirmToken\n\n" +
                "В противном случае, просто проигнорируйте это письмо.")

        mail.setTo(email)

        javaMailSender.send(mail)
    }

    @Throws(MailException::class)
    override fun sendPasswordRefreshMessage(email: String, refreshToken: String) {
        val mail = generateMail("Смена пароля в Трикстере.",
                "Чтобы восстановить пароль, перейдите по ссылке:\n" +
                "https://api.trickster.im/refresh/password/$refreshToken")

        mail.setTo(email)

        javaMailSender.send(mail)
    }

    private fun generateMail(subject: String, text: String): SimpleMailMessage {
        val mail = SimpleMailMessage()

        mail.subject = subject
        // TODO: Сделать так, чтобы "from" бралось из application.properties.
        mail.from = "service@trickster.im"
        mail.text = text

        return mail
    }
}
