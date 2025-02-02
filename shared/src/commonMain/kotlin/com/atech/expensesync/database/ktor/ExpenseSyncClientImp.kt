package com.atech.expensesync.database.ktor

import com.atech.expensesync.database.ktor.ExpenseSyncClient.Companion.BASE_URL
import com.atech.expensesync.database.models.User
import com.atech.expensesync.utils.ApiPaths
import com.atech.expensesync.utils.ApiResponse
import com.atech.expensesync.utils.ResponseDataState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ExpenseSyncClientImp(
    private val client: HttpClient
) : ExpenseSyncClient {
    override suspend fun logInUser(model: User): ResponseDataState<User> {
        try {
            val response: ApiResponse<User> = client.post {
                url("$BASE_URL/${ApiPaths.User.path}/create")
                contentType(ContentType.Application.Json)
                setBody(model)
            }.body()
            return ResponseDataState.wrapResponse(response.data)
        } catch (e: Exception) {
            return ResponseDataState.Error(e.message ?: "Unknown error")
        }
    }

}