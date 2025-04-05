package com.atech.expensesync.firebase.util

sealed interface FirebaseResponse<out T> {
    data class Success<T>(val data: T) : FirebaseResponse<T>
    data class Error(val error: String) : FirebaseResponse<Nothing>
    object Loading : FirebaseResponse<Nothing>
}