package co.in2all.trickster.api.server.controllers

import co.in2all.trickster.api.server.errors.ApiError
import co.in2all.trickster.api.server.repositories.BoardsRepository
import co.in2all.trickster.api.server.repositories.SessionsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/boards")
class BoardsController @Autowired constructor(
        val boardsRepository: BoardsRepository,
        val sessionsRepository: SessionsRepository) {

    @GetMapping("/{id}")
    @ResponseBody
    fun getById(
            @PathVariable(value = "id") id: Long,
            @RequestParam(value = "access_token") accessToken: String): Any {
        val session = sessionsRepository.get(accessToken)

        return when (session) {
            null -> ApiError.INVALID_ACCESS_TOKEN
            else -> boardsRepository.get(id) ?: ApiError.OBJECT_NOT_EXISTS
        }
    }
}
