package co.in2all.trickster.api.server.errors

object ApiError {
    // TODO: Перевести все ошибки правильно.
    val UNKNWON: Error = Error(1, "An unknown error occurred.")
    val AUTH: Error = Error(2, "Authorisation error. Wrong data were received.")
    val REFRESH: Error = Error(3, "Refreshing error. Wrong data were received.")
    val INVALID_ACCESS_TOKEN: Error = Error(4, "You passed a nonexistent access_token.")
    val OBJECT_NOT_EXIST: Error = Error(5, "The requested object does not exist.")
    val TOO_MANY_FACES: Error = Error(6, "The maximum number of faces has been exceeded.")
}
