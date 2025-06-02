package com.example.capyvocab_fe.core.error

import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

data class AppFailure(
    val error: AppError,
    val message: String? = null,
    val throwable: Throwable? = null
)

enum class AppError(val defaultMessage: String) {
    NetworkError("Cannot connect to server"),
    Unauthorized("You are not authorized"),
    Forbidden("Access denied"),
    NotFound("User not found"),
    ServerError("Server error"),
    Unknown("Unexpected error");

    companion object {
        fun fromHttpCode(code: Int): AppError = when (code) {
            401 -> Unauthorized
            403 -> Forbidden
            404 -> NotFound
            in 500..599 -> ServerError
            else -> Unknown
        }
    }
}

fun Throwable.toAppFailure(): AppFailure {
    val error = when (this) {
        is IOException -> AppError.NetworkError
        is HttpException -> AppError.fromHttpCode(this.code())
        else -> AppError.Unknown
    }

    val message = (this as? HttpException)?.response()?.errorBody()?.string()?.let { body ->
        try {
            JSONObject(body).optString("message", null)
        } catch (_: Exception) {
            null
        }
    }

    return AppFailure(
        error = error,
        message = message ?: error.defaultMessage,
        throwable = this
    )
}

