package com.atech.expensesync.login

import androidx.compose.runtime.Composable

data class LogInState(
    val uid: String?,
    val displayName: String?,
    val email: String?,
    val photoUrl: String?,
    val errorMessage: String?
)

@Composable
expect fun InvokeLogInWithGoogle(
    onLogInDone: (LogInState) -> Unit,
)