package co.in2all.trickster.api.server.controllers

import co.in2all.trickster.api.server.repositories.AppsRepository
import co.in2all.trickster.api.server.repositories.SessionsRepository
import co.in2all.trickster.api.server.repositories.UsersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/")
class AuthController @Autowired constructor(
        val appsRepository: AppsRepository,
        val usersRepository: UsersRepository,
        val sessionsRepository: SessionsRepository) {
    @GetMapping("/signin/{appId}")
    fun signInWithApp(
            @PathVariable(value = "appId") appId: String): ModelAndView {
        return if (appsRepository.getByClientId(appId) != null) {
            val model = hashMapOf("appId" to appId)
            ModelAndView("signin", model)
        } else {
            return ModelAndView("404", HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/signin/{appId}")
    fun createSessionAndRedirect(
            @PathVariable(value = "appId") appId: String,
            @RequestParam(value = "email", required = true) email: String,
            @RequestParam(value = "password", required = true) password: String): String {
        val client = appsRepository.getByClientId(appId)
        val user = usersRepository.getByEmailAndPassword(email, password)

        return if (client != null && user != null) {
            // TODO: Добавить генерацию данных для сессии.
            // Использовать, например, RandomStringUtils.randomAlphanumeric
            sessionsRepository.createSession(
                    user.email!!,
                    client.client_id!!,
                    "efeef",
                    "fefefe",
                    1)
            // TODO: А сюда в конец редиректа добавить параметр access_token.
            "redirect:${client.redirect_uri}"
        } else {
            "redirect:../404"
        }
    }

    @GetMapping("/auth/")
    fun getAccessTokenAndReturnSessionInfo() {
        // TODO: Реализовать.
    }
}
