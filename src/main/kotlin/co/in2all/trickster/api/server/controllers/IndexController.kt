package co.in2all.trickster.api.server.controllers

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class IndexController {
    @GetMapping("/")
    fun index(): ModelAndView {
        val model = hashMapOf("name" to "dude")
        return ModelAndView("index", model)
    }

    @GetMapping("404")
    fun notFoundError(): ModelAndView {
        return ModelAndView("404", HttpStatus.NOT_FOUND)
    }
}
