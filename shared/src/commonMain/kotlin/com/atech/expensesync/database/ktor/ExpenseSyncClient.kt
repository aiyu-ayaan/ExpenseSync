package com.atech.expensesync.database.ktor

import com.atech.expensesync.database.models.User
import com.atech.expensesync.utils.ResponseDataState

interface ExpenseSyncClient {
    suspend fun logInUser(model: User): ResponseDataState<User>

    companion object {
        const val BASE_URL = "http://192.168.9.65:9090"
    }
}