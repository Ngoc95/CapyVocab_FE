package com.example.capyvocab_fe.auth.domain.model

data class AuthFailure(
    val error: ApiError,
    val message: String? = null, // Lưu message từ backend
    val t: Throwable? = null
)

enum class ApiError(val defaultMessage: String) {
    NetworkError("Network Error"),
    BadRequest("Bad Request"),
    Unauthorized("Unauthorized"),
    Forbidden("Forbidden"),
    NotFound("Not Found"),
    ServerError("Internal Server Error"),
    UnknownResponse("Unknown Response"),
    UnknownError("Unknown Error");

    companion object {
        fun fromHttpCode(code: Int): ApiError {
            return when (code) {
                400 -> BadRequest
                401 -> Unauthorized
                403 -> Forbidden
                404 -> NotFound
                in 500..599 -> ServerError
                else -> UnknownResponse
            }
        }
    }
}
