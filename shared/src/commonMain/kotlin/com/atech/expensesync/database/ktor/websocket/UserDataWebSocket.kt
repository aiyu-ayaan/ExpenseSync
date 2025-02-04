package com.atech.expensesync.database.ktor.websocket

import com.atech.expensesync.database.models.User
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class UserDataWebSocket(
    private val client: HttpClient,
    private val json: Json = Json { ignoreUnknownKeys = true }
) {
    private var session: WebSocketSession? = null

    fun getStateStream(desktopId: String): Flow<User?> = flow {
        try {
            session = client.webSocketSession {
                url("ws://localhost:9090/ws/v1/user/desktop/$desktopId")
            }
            val user = session!!.incoming
                .consumeAsFlow()
                .filterIsInstance<Frame.Text>()
                .map { json.decodeFromString(User.serializer(), it.readText()) }
            emitAll(user)
        } catch (e: Exception) {
            e.printStackTrace()
            // Consider adding reconnection logic or error handling
        }
    }
}