package co.in2all.trickster.api.server.controller

import co.in2all.trickster.api.server.error.ApiError
import co.in2all.trickster.api.server.repository.*
import co.in2all.trickster.api.server.service.NotificationService
import co.in2all.trickster.api.server.utility.Safeguard
import com.typesafe.config.ConfigFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.mail.MailException
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import java.util.*

@Controller
@RequestMapping("/")
class AuthController @Autowired constructor(
        val appsRepository: AppsRepository,
        val usersRepository: UsersRepository,
        val authTokensRepository: AuthTokensRepository,
        val sessionsRepository: SessionsRepository,
        val signupDataRepository: SignupDataRepository,
        val notificationService: NotificationService) {

    @GetMapping("/signin/{client_id}")
    fun signInWithApp(@PathVariable(value = "client_id") clientId: String): ModelAndView {
        val app = appsRepository.get(clientId)

        return when (app) {
            null -> ModelAndView("404", HttpStatus.NOT_FOUND)
            else -> ModelAndView("signin", hashMapOf(
                    "client_id" to app.client_id,
                    "app_name" to app.name))
        }
    }

    @PostMapping("/signin/{client_id}")
    fun createAuthTokenAndRedirect(
            @PathVariable(value = "client_id") clientId: String,
            @RequestParam(value = "email") email: String,
            @RequestParam(value = "password") password: String): Any {
        val app = appsRepository.get(clientId)

        // TODO-misonijnik: Тут проверяется существование юзера с переданным паролем. Добавить шифрование.
        val user = usersRepository.get(email, password)

        return when {
            app == null ->
                // TODO: Не уверен, что в этом случае нужно отдавать 404. Разобраться.
                ModelAndView("404", HttpStatus.NOT_FOUND)
            user == null ->
                // TODO: Сообщение о неверном логине или пароле и просьба повторить их ввод.
                "redirect:/signin/$clientId"
            else -> {
                val token = Safeguard.getUniqueToken()
                val expiresIn = Date().time + ConfigFactory.load().getLong("auth_token.lifetime")

                authTokensRepository.create(user.email!!, app.client_id!!, token, expiresIn)

                "redirect:${app.redirect_uri}/$token"
            }
        }
    }

    @GetMapping("/signup/{client_id}")
    fun signUpWithApp(
            @PathVariable(value = "client_id") clientId: String): ModelAndView {
        val app = appsRepository.get(clientId)

        return when (app) {
            null -> ModelAndView("404", HttpStatus.NOT_FOUND)
            else -> ModelAndView("signup", hashMapOf(
                    "client_id" to app.client_id,
                    "app_name" to app.name))
        }
    }

    @PostMapping("/signup/{client_id}")
    fun createSignupTokenAndEmail(
            @PathVariable(value = "client_id") clientId: String,
            @RequestParam(value = "email") email: String,
            @RequestParam(value = "password") password: String): Any {
        val app = appsRepository.get(clientId)
        val user = usersRepository.get(email)

        return when {
            app == null ->
                // TODO: Сообщение о неверном логине или пароле и просьба повторить их ввод.
                // Сделать после появления дизайна страницы.
                "redirect:/signup/$clientId"
            user != null ->
                // TODO: Сообщение о том, что пользователь уже существует.
                // Сделать после появления дизайна страницы.
                "redirect:/signup/$clientId"
            else -> {
                val token = Safeguard.getUniqueToken()
                val expiresIn = Date().time + ConfigFactory.load().getLong("signup_token.lifetime")

                // TODO-misonijnik: Тут сохраняется пароль пользователя. Добавь шифрование.
                signupDataRepository.create(email, password, clientId, token, expiresIn)

                try {
                    notificationService.sendEmailConfirmMessage(email, token)
                }
                catch (e: MailException) {
                    // TODO: Сообщение о неверном email.
                    return "redirect:/signup/$clientId"
                }

                ModelAndView("check-mailbox")
            }
        }
    }

    @GetMapping("/confirm/{token}")
    fun createUserAndAuthToken(
            @PathVariable(value = "token") token: String): Any {
        val signupData = signupDataRepository.get(token) ?: return ModelAndView("404", HttpStatus.NOT_FOUND)
        val app = appsRepository.get(signupData.client_id!!)

        return when (app) {
            null -> ModelAndView("404", HttpStatus.NOT_FOUND)
            else -> {
                usersRepository.create(signupData.email!!, signupData.password!!)

                val authToken = Safeguard.getUniqueToken()
                val expiresIn = Date().time + ConfigFactory.load().getLong("auth_token.lifetime")

                authTokensRepository.create(signupData.email!!, signupData.client_id!!, authToken, expiresIn)

                signupDataRepository.delete(signupData.token!!)
                "redirect:${app.redirect_uri}/$token"
            }
        }
    }

    @GetMapping("/auth")
    @ResponseBody
    fun createAndGetSession(
            @RequestParam(value = "auth_token") authToken: String,
            @RequestParam(value = "client_id") clientId: String,
            @RequestParam(value = "client_secret") clientSecret: String): Any {
        val authTokenObject = authTokensRepository.get(authToken, clientId, clientSecret)

        return when (authTokenObject) {
            null -> ApiError.AUTH
            else -> {
                val accessToken = Safeguard.getUniqueToken()
                val expiresIn = Date().time + ConfigFactory.load().getLong("access_token.lifetime")
                val refreshToken = Safeguard.getUniqueToken()

                sessionsRepository.create(authToken, accessToken, expiresIn, refreshToken)
                sessionsRepository.get(accessToken)!!
            }
        }
    }

    @GetMapping("/refresh")
    @ResponseBody
    fun refreshAndReturnSession(
            @RequestParam(value = "refresh_token") refreshToken: String,
            @RequestParam(value = "client_id") clientId: String,
            @RequestParam(value = "client_secret") clientSecret: String): Any {
        val session = sessionsRepository.get(refreshToken, clientId, clientSecret)

        return when (session) {
            null -> ApiError.REFRESH
            else -> {
                val newAccessToken = Safeguard.getUniqueToken()
                val newExpiresIn = Date().time + ConfigFactory.load().getLong("access_token.lifetime")
                val newRefreshToken = Safeguard.getUniqueToken()

                sessionsRepository.refresh(refreshToken, newAccessToken, newExpiresIn, newRefreshToken)
            }
        }
    }
}
