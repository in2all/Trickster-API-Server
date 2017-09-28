package co.in2all.trickster.api.server.controllers

import co.in2all.trickster.api.server.repositories.AppsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/")
class AuthController @Autowired constructor(val appsRepository: AppsRepository) {
    @GetMapping("/signin/{appId}")
    fun signInWithApp(
            @PathVariable(value = "appId") appId: String): ModelAndView {
        return if (!appsRepository.getByClientId(appId).isEmpty()) {
            val model = hashMapOf("appId" to appId)
            ModelAndView("signin", model)
        } else {
            return ModelAndView("404", HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/signin/{appId}")
    fun createSessionAndRedirect(
            @PathVariable(value = "appId") appId: String): String {
        val clients = appsRepository.getByClientId(appId)

        // TODO: Создание сессии.

        return if (!clients.isEmpty()) {
            "redirect:${clients[0].redirect_uri}"
        } else {
            "redirect:../404"
        }
    }
}
