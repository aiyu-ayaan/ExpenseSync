package com.atech.expensesync.firebase.util

sealed interface FirebaseResponse<out T> {
    data class Success<T>(val data: T) : FirebaseResponse<T>
    data class Error(val error: String) : FirebaseResponse<Nothing>
    object Loading : FirebaseResponse<Nothing>
}

fun FirebaseResponse<*>.isLoading(): Boolean {
    return this is FirebaseResponse.Loading
}

fun FirebaseResponse<*>.isError(): Boolean {
    return this is FirebaseResponse.Error
}

fun FirebaseResponse<*>.isSuccess(): Boolean {
    return this is FirebaseResponse.Success
}

fun <T> FirebaseResponse<T>.getOrNull(): T? {
    return when (this) {
        is FirebaseResponse.Success -> this.data
        else -> null
    }
}