package com.atech.expensesync.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

actual fun initFirebase() {
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