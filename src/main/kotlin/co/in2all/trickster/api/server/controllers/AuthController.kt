package co.in2all.trickster.api.server.controllers

import co.in2all.trickster.api.server.errors.ApiError
import co.in2all.trickster.api.server.repositories.AppsRepository
import co.in2all.trickster.api.server.repositories.AuthTokensRepository
import co.in2all.trickster.api.server.repositories.SessionsRepository
import co.in2all.trickster.api.server.repositories.UsersRepository
import co.in2all.trickster.api.server.utils.Safeguard
import com.typesafe.config.ConfigFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
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
        var sessionsRepository: SessionsRepository) {

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
    fun createAccessTokenAndRedirect(
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
        return when {
            appsRepository.get(clientId) == null -> ModelAndView("404", HttpStatus.NOT_FOUND)
            else -> ModelAndView("signup", hashMapOf("client_id" to clientId))
        }
    }

    @PostMapping("/signup/{client_id}")
    fun createUserAndAccessTokenAndRedirect(
            @PathVariable(value = "client_id") clientId: String,
            @RequestParam(value = "email") email: String,
            @RequestParam(value = "password") password: String): String {
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
                val expiresIn = Date().time + ConfigFactory.load().getLong("auth_token.lifetime")

                // TODO-misonijnik: Добавить шифрование пароля перед записью в БД.
                usersRepository.create(email, password)
                authTokensRepository.create(email, clientId, token, expiresIn)

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
