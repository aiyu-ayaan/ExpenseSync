package com.atech.expensesync.login

import androidx.compose.runtime.Composable
import com.atech.expensesync.database.models.User

data class LogInState(
    val uid: String?,
    val displayName: String?,
    val email: String?,
    val photoUrl: String?,
    val errorMessage: String?
)

fun LogInState.toUser() = User(
    uid = uid ?: "",
    name = displayName ?: "",
    email = email ?: "",
    photoUrl = photoUrl ?: "",
)

@Composable
expect fun InvokeLogInWithGoogle(
    onLogInDone: (LogInState) -> Unit,
)