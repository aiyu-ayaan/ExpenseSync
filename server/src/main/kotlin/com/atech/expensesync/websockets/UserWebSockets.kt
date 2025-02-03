package com.atech.expensesync.websockets

import com.atech.expensesync.database.models.User
import com.atech.expensesync.firebase.FirebaseInstance
import com.atech.expensesync.utils.FirebaseCollectionPath
import com.atech.expensesync.utils.WebSocketPaths
import com.atech.expensesync.utils.toJson
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
fun Application.userWebSocket() {
    routing {
        webSocket("${WebSocketPaths.User.path}/desktop/{desktopId}") {
            val desktopId = call.parameters["desktopId"]
            val db = FirebaseInstance.firestore

            val listenerRegistration = db.collection(FirebaseCollectionPath.USER.path)
                .whereEqualTo("systemUid", desktopId)
                .addSnapshotListener { value, error ->
                    GlobalScope.launch(Dispatchers.IO) {
                        error?.let {
                            close(CloseReason(CloseReason.Codes.NORMAL, "Error: ${it.message}"))
                            return@launch
                        }

                        send(Frame.Text(value?.toObjects(User::class.java)?.last().toJson()))
                    }
                }

            try {
                // Keep WebSocket connection open
                for (frame in incoming) {
                    // Optional: Handle incoming messages
                }
            } finally {
                // Remove Firestore listener when WebSocket closes
                listenerRegistration.remove()
            }
        }
    }
}