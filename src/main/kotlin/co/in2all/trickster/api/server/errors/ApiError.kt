package co.in2all.trickster.api.server.errors

object ApiError {
    val UNKNWON: Error = Error(1, "An unknown error occurred.")
    val AUTH: Error = Error(2, "Authorisation error. Wrong data were received.")
    val REFRESH: Error = Error(3, "Refreshing error. Wrong data were received.")
}
