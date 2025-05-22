package htps.death.htpswebsitekotlin.response

data class ResponseWrapper<T>(
    val message: String, val data: T? = null
) {
    companion object {
        fun <T> success(data: T): ResponseWrapper<T> = ResponseWrapper("success", data)

        fun <T> error(message: String): ResponseWrapper<T> = ResponseWrapper(message, null)
    }
}