package com.atech.expensesync.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun InvokeLogInWithGoogle(onLogInDone: (LogInState) -> Unit) {
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {

        }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    GoogleSignInUtils.doGoogleSignIn(
        context = context,
        scope = coroutineScope,
        launcher = launcher,
        logIn = { user, error ->
            if (user != null) {
                onLogInDone(
                    LogInState(
                        uid = user.uid,
                        displayName = user.displayName,
                        email = user.email,
                        photoUrl = user.photoUrl.toString(),
                        errorMessage = null
                    )
                )
            } else {
                onLogInDone(
                    LogInState(
                        uid = null,
                        displayName = null,
                        email = null,
                        photoUrl = null,
                        errorMessage = error?.message
                    )
                )
            }
        }
    )
}