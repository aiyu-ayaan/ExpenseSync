package com.atech.expensesync.login

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialOption
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.atech.expensesync.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

object GoogleSignInUtils {

    fun doGoogleSignIn(
        context: Context,
        scope: CoroutineScope,
        launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
        logIn: (FirebaseUser?, Exception?) -> Unit
    ) {
        val credentialManager = CredentialManager.create(context)
        val request =
            GetCredentialRequest.Builder().addCredentialOption(getCredentialOption())
                .build()
        scope.launch {
            try {
                val result = credentialManager.getCredential(context, request)
                when (result.credential) {
                    is CustomCredential -> {
                        if (result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                            val googleIdTokenCredential =
                                GoogleIdTokenCredential.createFrom(result.credential.data)
                            val googleTokenId = googleIdTokenCredential.idToken
                            val authCredential = GoogleAuthProvider.getCredential(
                                googleTokenId,
                                null
                            )
                            val user =
                                Firebase.auth.signInWithCredential(authCredential).await().user
                            user?.let { user ->
                                if (user.isAnonymous.not()) {
                                    logIn(user, null)
                                }
                            }
                        }
                    }
                }
            } catch (e: NoCredentialException) {
                launcher?.launch(signToNewAccount())
            } catch (e: GetCredentialException) {
                e.printStackTrace()
                logIn(null, e)
            }
        }
    }

    fun signToNewAccount(): Intent =
        Intent(Settings.ACTION_ADD_ACCOUNT).apply {
            putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
        }

    fun getCredentialOption(): CredentialOption =
        GetGoogleIdOption.Builder().setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(false).setServerClientId(BuildConfig.firebaseWebClient)
            .build()

}