package co.in2all.trickster.api.server.controllers

import co.in2all.trickster.api.server.errors.ApiError
import co.in2all.trickster.api.server.repositories.FacesRepository
import co.in2all.trickster.api.server.repositories.SessionsRepository
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
        return if (sessionsRepository.get(accessToken) != null) {
            facesRepository.getAll(accessToken)
        } else {
            ApiError.INVALID_ACCESS_TOKEN
        }
    }

    @GetMapping("/{name}")
    @ResponseBody
    fun getByName(
            @PathVariable(value = "name") name: String,
            @RequestParam(value = "access_token") accessToken: String): Any {
        return if (sessionsRepository.get(accessToken) != null) {
            facesRepository.get(name) ?: ApiError.OBJECT_NOT_EXIST
        } else {
            ApiError.INVALID_ACCESS_TOKEN
        }
    }

    @PostMapping("")
    @ResponseBody
    fun create(@RequestParam(value = "access_token") accessToken: String,
               @RequestParam(value = "name") name: String,
               @RequestParam(value = "description", required = false) description: String?,
               @RequestParam(value = "avatar", required = false) avatar: String?): Any {
        // TODO: Засунуть в конфиг.
        val maxFacesNumber = 20

        return if (sessionsRepository.get(accessToken) != null) {
            return if (facesRepository.count(accessToken) <= maxFacesNumber) {
                // TODO: Добавить проверку аватара на легитимность.
                facesRepository.create(
                        accessToken,
                        name,
                        description ?: "",
                        avatar ?: "")

                HttpStatus.OK
            } else {
                ApiError.TOO_MANY_FACES
            }
        } else {
            ApiError.INVALID_ACCESS_TOKEN
        }
    }
}