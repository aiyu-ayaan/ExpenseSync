package com.atech.expensesync

import com.atech.expensesync.plugins.configureCaching
import com.atech.expensesync.plugins.configureMonitoring
import com.atech.expensesync.plugins.configureSerialization
import com.atech.expensesync.plugins.configureWebSocket
import com.atech.expensesync.routes.configureRouting
import com.atech.expensesync.utils.SERVER_PORT
import com.atech.expensesync.websockets.entryWebSocket
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty


fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    initFirebase()
    configureMonitoring()
    configureCaching()
    configureSerialization()
    configureWebSocket()
    configureRouting()
    entryWebSocket()
}

private fun Application.initFirebase() {
    try {
        val clientSecreteStream = object {}.javaClass.getResourceAsStream("/web-client.json")
            ?: throw Exception("web-client.json not found")
        val option = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(clientSecreteStream))
            .build()

        FirebaseApp.initializeApp(option)
    } catch (e: Exception) {
        throw Exception("Failed to initialize Firebase Admin: ${e.message}")
    }
}