package co.in2all.trickster.api.server.errors

object ApiError {
    val UNKNWON: Error = Error(1, "An unknown error occurred.")
    val AUTH: Error = Error(2, "Authorisation Error. Wrong data were received.")
}
