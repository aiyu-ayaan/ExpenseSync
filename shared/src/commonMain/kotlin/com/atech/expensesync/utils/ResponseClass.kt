package com.atech.expensesync.utils

import kotlinx.serialization.Serializable

@Serializable
data class ResponseClass<T>(
    @Serializable
    val response: T?,
    val error: String?
)

@Serializable
sealed class ResponseDataState<T> {
    @Serializable
    data class Success<T>(val data: T) : ResponseDataState<T>()

    @Serializable
    data class Error(val error: String) : ResponseDataState<Nothing>()
}