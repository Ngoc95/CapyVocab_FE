package com.example.capyvocab_fe.admin.user.domain.error

import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

data class AdminFailure(
    val error: AdminError,
    val message: String? = null,
    val throwable: Throwable? = null
)

enum class AdminError(val defaultMessage: String) {
    NetworkError("Cannot connect to server"),
    Unauthorized("You are not authorized"),
    Forbidden("Access denied"),
    NotFound("User not found"),
    ServerError("Server error"),
    Unknown("Unexpected error");

    companion object {
        fun fromHttpCode(code: Int): AdminError = when (code) {
            401 -> Unauthorized
            403 -> Forbidden
            404 -> NotFound
            in 500..599 -> ServerError
            else -> Unknown
        }
    }
}

fun Throwable.toAdminFailure(): AdminFailure {
    val error = when (this) {
        is IOException -> AdminError.NetworkError
        is HttpException -> AdminError.fromHttpCode(this.code())
        else -> AdminError.Unknown
    }

    val message = (this as? HttpException)?.response()?.errorBody()?.string()?.let { body ->
        try {
            JSONObject(body).optString("message", null)
        } catch (_: Exception) {
            null
        }
    }

    return AdminFailure(
        error = error,
        message = message ?: error.defaultMessage,
        throwable = this
    )
}

