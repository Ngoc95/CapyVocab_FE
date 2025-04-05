package com.example.capyvocab_fe.auth.data.mapper

import com.example.capyvocab_fe.auth.domain.model.ApiError
import com.example.capyvocab_fe.auth.domain.model.AuthFailure
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

fun Throwable.toAuthFailure(): AuthFailure {
    val error = when (this) {
        is IOException -> ApiError.NetworkError
        is HttpException -> ApiError.fromHttpCode(this.code()) // Xử lý lỗi theo mã HTTP
        else -> ApiError.UnknownError
    }

    // Lấy message từ response body nếu có
    val message = (this as? HttpException)?.response()?.errorBody()?.string()?.let { body ->
        try {
            JSONObject(body).optString("message", null) // Lấy giá trị "message"
        } catch (e: Exception) {
            null
        }
    }

    return AuthFailure(
        error = error,
        message = message ?: error.defaultMessage, // Nếu không có thì dùng default message
        t = this
    )
}