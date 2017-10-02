package co.in2all.trickster.api.server.controllers

import co.in2all.trickster.api.server.errors.ApiError
import co.in2all.trickster.api.server.repositories.AppsRepository
import co.in2all.trickster.api.server.repositories.AuthTokensRepository
import co.in2all.trickster.api.server.repositories.SessionsRepository
import co.in2all.trickster.api.server.repositories.UsersRepository
import co.in2all.trickster.api.server.utils.Safeguard
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import java.util.*
import java.util.concurrent.TimeUnit

@Controller
@RequestMapping("/")
class AuthController @Autowired constructor(
        val appsRepository: AppsRepository,
        val usersRepository: UsersRepository,
        val authTokensRepository: AuthTokensRepository,
        var sessionsRepository: SessionsRepository) {

    @GetMapping("/signin/{client_id}")
    fun signInWithApp(@PathVariable(value = "client_id") clientId: String): ModelAndView {
        return if (appsRepository.get(clientId) != null) {
            ModelAndView("signin", hashMapOf("client_id" to clientId))
        } else {
            ModelAndView("404", HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/signin/{client_id}")
    fun createAccessTokenAndRedirect(
            @PathVariable(value = "client_id") clientId: String,
            @RequestParam(value = "email") email: String,
            @RequestParam(value = "password") password: String): String {
        val app = appsRepository.get(clientId)
        // TODO-misonijnik: Тут проверяется существование юзера с переданным паролем. Добавить шифрование.
        val user = usersRepository.get(email, password)

        // TODO: Сделать что-нибудь с мутабельностью полей user!
        return if (app != null && user != null && user.email != null) {
            val token = Safeguard.getUniqueToken()
            // TODO: Перенести время expiresIn в конфиги.
            val expiresIn = Date().time + TimeUnit.MINUTES.toMillis(10)

            authTokensRepository.create(user.email!!, app.client_id!!, token, expiresIn)

            "redirect:${app.redirect_uri}/$token"
        } else {
            // TODO: Сообщение о неверном логине или пароле и просьба повторить их ввод.
            // Сделать после появления дизайна страницы.
            "redirect:/signin/$clientId"
        }
    }

    @GetMapping("/signup/{client_id}")
    fun signUpWithApp(
            @PathVariable(value = "client_id") clientId: String): ModelAndView {
        return if (appsRepository.get(clientId) != null) {
            ModelAndView("signup", hashMapOf("client_id" to clientId))
        } else {
            ModelAndView("404", HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/signup/{client_id}")
    fun createUserAndAccessTokenAndRedirect(
            @PathVariable(value = "client_id") clientId: String,
            @RequestParam(value = "email") email: String,
            @RequestParam(value = "password") password: String): String {
        val app = appsRepository.get(clientId)

        return if (app != null) {
            return if (usersRepository.get(email) == null) {
                val token = Safeguard.getUniqueToken()
                // TODO: Перенести время expiresIn в конфиги.
                val expiresIn = Date().time + TimeUnit.MINUTES.toMillis(10)

                // TODO-misonijnik: Добавить шифрование пароля перед записью в БД.
                usersRepository.create(email, password)
                authTokensRepository.create(email, clientId, token, expiresIn)

                "redirect:${app.redirect_uri}/$token"
            } else {
                // TODO: Сообщение о том, что пользователь уже существует.
                // Сделать после появления дизайна страницы.
                "redirect:/signup/$clientId"
            }
        } else {
            // TODO: Сообщение о неверном логине или пароле и просьба повторить их ввод.
            // Сделать после появления дизайна страницы.
            "redirect:/signup/$clientId"
        }
    }

    @GetMapping("/auth")
    @ResponseBody
    fun createAndGetSession(
            @RequestParam(value = "auth_token") authToken: String,
            @RequestParam(value = "client_id") clientId: String,
            @RequestParam(value = "client_secret") clientSecret: String): Any {
        return if (authTokensRepository.get(authToken, clientId, clientSecret) != null) {
            val accessToken = Safeguard.getUniqueToken()
            val expiresIn = Date().time + TimeUnit.DAYS.toMillis(1)
            val refreshToken = Safeguard.getUniqueToken()

            sessionsRepository.create(authToken, accessToken, expiresIn, refreshToken)
            sessionsRepository.get(accessToken)!!
        } else {
            ApiError.AUTH
        }
    }

    @GetMapping("/refresh")
    @ResponseBody
    fun refreshAndReturnSession(
            @RequestParam(value = "refresh_token") refreshToken: String,
            @RequestParam(value = "client_id") clientId: String,
            @RequestParam(value = "client_secret") clientSecret: String): Any {
        return if (sessionsRepository.get(refreshToken, clientId, clientSecret) != null) {
            val newAccessToken = Safeguard.getUniqueToken()
            // TODO: Перенести время expiresIn в конфиги.
            val newExpiresIn = Date().time + TimeUnit.DAYS.toMillis(1)
            val newRefreshToken = Safeguard.getUniqueToken()

            sessionsRepository.refresh(refreshToken, newAccessToken, newExpiresIn, newRefreshToken)
        } else {
            ApiError.REFRESH
        }
    }
}
