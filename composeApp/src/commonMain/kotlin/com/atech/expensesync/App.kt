package com.atech.expensesync

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.atech.expensesync.database.models.User
import com.atech.expensesync.database.pref.PrefKeys
import com.atech.expensesync.database.pref.PrefManager
import com.atech.expensesync.navigation.ExpanseSyncNavigation
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.atech.expensesync.utils.FirebaseCollectionPath
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

val LocalDataStore = staticCompositionLocalOf<PrefManager> { error("No DataStore provided") }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App(
    navHostController: NavHostController = rememberNavController()
) {
    com.atech.expensesync.firebase.initFirebase() // init firebase
    KoinContext {
        ExpenseSyncTheme {
            val pref = koinInject<PrefManager>()
            val kmpFire = koinInject<com.atech.expensesync.firebase.io.KmpFire>()
            LaunchedEffect(true) {
                kmpFire.fetchData<User>(
                    collectionName = FirebaseCollectionPath.USER.path,
                    documentName = "087LOw0ndHUjyQwO77UIY6kdFt42"
                ).collect {
                    when (it) {
                        is com.atech.expensesync.firebase.util.FirebaseResponse.Error -> {
                            com.atech.expensesync.utils.expenseSyncLogger(
                                "Error: ${it.error}"
                            )
                        }

                        is com.atech.expensesync.firebase.util.FirebaseResponse.Loading -> {
                            com.atech.expensesync.utils.expenseSyncLogger(
                                "Loading ..."
                            )
                        }

                        is com.atech.expensesync.firebase.util.FirebaseResponse.Success -> {
                            com.atech.expensesync.utils.expenseSyncLogger(
                                "Success: ${it.data}"
                            )
                        }
                    }
                }
            }
            CompositionLocalProvider(
                LocalDataStore provides pref
            ) {
                val isLoggedInSkipped = pref.getBoolean(PrefKeys.IS_LOG_IN_SKIP)
                val userId = pref.getString(PrefKeys.USER_ID)
                ExpanseSyncNavigation(
                    modifier = Modifier.padding(
                    ),
                    navHostController = navHostController,
                    startDestination = if (isLoggedInSkipped || userId.isNotEmpty())
                        ExpanseSyncNavigation.AppScreens.route
                    else
                        ExpanseSyncNavigation.LogInScreen.route

                )
            }
        }
    }
}