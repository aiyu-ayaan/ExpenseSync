package com.atech.expensesync.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.oauth2.Oauth2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStreamReader


@Composable
actual fun InvokeLogInWithGoogle(onLogInDone: (LogInState) -> Unit) {
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            isLoading = true
            val loginState = withContext(Dispatchers.IO) {
                performGoogleLogin()
            }
            onLogInDone(loginState)
        } catch (e: Exception) {
            onLogInDone(
                LogInState(
                    uid = null,
                    displayName = null,
                    email = null,
                    photoUrl = null,
                    errorMessage = e.message
                )
            )
        } finally {
            isLoading = false
        }
    }

    // You might want to show a loading indicator while authentication is in progress
    if (isLoading) {
        Dialog(onDismissRequest = { /*TODO*/ }) {
            Box(
                modifier = Modifier.padding(
                    16.dp
                )
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

private suspend fun performGoogleLogin(): LogInState = withContext(Dispatchers.IO) {
    val clientSecreteStream = object {}.javaClass.getResourceAsStream("/client_secrets.json")
        ?: throw Exception("client_secret.json not found")

    val clientSecrets = try {
        GoogleClientSecrets.load(
            GsonFactory.getDefaultInstance(), InputStreamReader(clientSecreteStream)
        )
    } catch (e: Exception) {
        throw Exception("Invalid client_secrets.json format: ${e.message}")
    }

    val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
    val dataStoreFactory = FileDataStoreFactory(File("tokens"))

    val flow = GoogleAuthorizationCodeFlow.Builder(
        httpTransport,
        GsonFactory.getDefaultInstance(),
        clientSecrets,
        listOf(
            "https://www.googleapis.com/auth/userinfo.profile",
            "https://www.googleapis.com/auth/userinfo.email"
        )
    ).setDataStoreFactory(dataStoreFactory)
        .setAccessType("offline")
        .build()

    val receiver = LocalServerReceiver.Builder()
        .setHost("localhost")
        .setPort(5000)
        .setCallbackPath("/")
        .build()

    val credential = AuthorizationCodeInstalledApp(flow, receiver).authorize("user")

    val oauth2 = Oauth2.Builder(httpTransport, GsonFactory.getDefaultInstance(), credential)
        .setApplicationName("Expanse Sync")
        .build()

    val userInfo = oauth2.userinfo().get().execute()
    print("userInfo: $userInfo")
    return@withContext LogInState(
        uid = userInfo.id,
        displayName = userInfo.name,
        email = userInfo.email,
        photoUrl = userInfo.picture,
        errorMessage = null
    )
}