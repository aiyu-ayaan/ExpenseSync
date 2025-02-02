package com.atech.expensesync.utils

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val data: T
)

@Serializable
sealed class ResponseDataState<out T> {
    @SerialName("success")
    @Serializable
    data class Success<T>(val data: T) : ResponseDataState<T>()

    @Serializable
    @SerialName("error")
    data class Error(val error: String) : ResponseDataState<Nothing>()

    companion object {
        // Helper function to wrap raw response
        inline fun <reified T> wrapResponse(response: T): ResponseDataState<T> {
            return Success(response)
        }
    }
}