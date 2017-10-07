package co.in2all.trickster.api.server.controllers

import co.in2all.trickster.api.server.errors.ApiError
import co.in2all.trickster.api.server.repositories.FacesRepository
import co.in2all.trickster.api.server.repositories.SessionsRepository
import com.typesafe.config.ConfigFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/faces")
class FacesController @Autowired constructor(
        val facesRepository: FacesRepository,
        val sessionsRepository: SessionsRepository) {

    @GetMapping("")
    @ResponseBody
    fun getAllFaces(
            @RequestParam(value = "access_token") accessToken: String): Any {
        val session = sessionsRepository.get(accessToken)

        return when (session) {
            null -> ApiError.INVALID_ACCESS_TOKEN
            else -> facesRepository.getAll(accessToken)
        }
    }

    @GetMapping("/{name}")
    @ResponseBody
    fun getByName(
            @PathVariable(value = "name") name: String,
            @RequestParam(value = "access_token") accessToken: String): Any {
        val session = sessionsRepository.get(accessToken)

        return when (session) {
            null -> ApiError.INVALID_ACCESS_TOKEN
            else -> facesRepository.get(name) ?: ApiError.OBJECT_NOT_EXISTS
        }
    }

    @PostMapping("/{name}")
    @ResponseBody
    fun create(@PathVariable(value = "name") name: String,
               @RequestParam(value = "access_token") accessToken: String,
               @RequestParam(value = "description", required = false) description: String?,
               @RequestParam(value = "avatar", required = false) avatar: String?): Any {
        val session = sessionsRepository.get(accessToken)
        val face = facesRepository.get(name)
        val facesCount = facesRepository.count(accessToken)
        val maxFacesNumber = ConfigFactory.load().getLong("faces.max_number")

        return when {
            session == null -> ApiError.INVALID_ACCESS_TOKEN
            face != null -> ApiError.OBJECT_ALREADY_EXISTS
            facesCount >= maxFacesNumber -> ApiError.TOO_MANY_FACES
            else -> {
                // TODO: Добавить проверку аватара на легитимность.
                facesRepository.create(
                        accessToken,
                        name,
                        description ?: "",
                        avatar ?: "")

                HttpStatus.OK
            }
        }
    }
}
