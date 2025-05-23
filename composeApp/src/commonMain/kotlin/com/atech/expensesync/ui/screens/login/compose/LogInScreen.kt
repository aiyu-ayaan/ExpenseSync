package com.atech.expensesync.ui.screens.login.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.atech.expensesync.LocalDataStore
import com.atech.expensesync.component.AppButton
import com.atech.expensesync.component.GoogleButton
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.models.User
import com.atech.expensesync.database.pref.PrefKeys
import com.atech.expensesync.firebase.util.FirebaseResponse
import com.atech.expensesync.login.toUser
import com.atech.expensesync.navigation.ExpanseSyncRoutes
import com.atech.expensesync.navigation.LogInNavigation
import com.atech.expensesync.ui.screens.login.LogInEvents
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.runWithDeviceCompose
import com.atech.expensesync.utils.expenseSyncLogger
import com.atech.expensesync.utils.runWithDevice
import com.atech.expensesync.utils.toJson
import expensesync.composeapp.generated.resources.Res
import expensesync.composeapp.generated.resources.login
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    userState: User? = null,
    onEvent: (LogInEvents) -> Unit,
    destroyViewModelObject: @Composable () -> Unit = {},
) {
    val pref = LocalDataStore.current
    var logInMessage by rememberSaveable { mutableStateOf("Creating ...") }
    var hasClick by rememberSaveable { mutableStateOf(false) }
    var canShowSnackBar by rememberSaveable { mutableStateOf(Pair<Boolean, String?>(false, null)) }
//    Set UID for desktop
    runWithDevice(onDesktop = {
        if (pref.getString(PrefKeys.DESKTOP_USER_UID).isBlank()) {
            pref.saveString(
                PrefKeys.DESKTOP_USER_UID,
                UUID.fromString(com.atech.expensesync.utils.getMachineUUID()).toString()
            )
        }
    })
    runWithDeviceCompose(
        onAndroid = {
            if (hasClick) {
                com.atech.expensesync.login.InvokeLogInWithGoogle { logInState ->
                    if (logInState.errorMessage != null) {
//                Todo: show error message
                        expenseSyncLogger(
                            "Error: ${logInState.errorMessage}"
                        )
                        canShowSnackBar = true to logInState.errorMessage
                        hasClick = false
                        return@InvokeLogInWithGoogle
                    }
                    onEvent.invoke(LogInEvents.OnLogInClicked(logInState.toUser()) { model ->
                        when (model) {
                            is FirebaseResponse.Error -> {
//                        Todo: show error message
                                canShowSnackBar = true to model.error
                                expenseSyncLogger(
                                    "Error: ${model.error}"
                                )
                            }

                            is FirebaseResponse.Success<User> -> {
                                pref.saveString(
                                    PrefKeys.USER_ID, model.data.uid
                                )
                                pref.saveString(
                                    PrefKeys.USER_MODEL, model.data.toJson()
                                )
                                navHostController.navigate(LogInNavigation.BackUpScreen.route) {
                                    launchSingleTop = true
                                    popUpTo(ExpanseSyncRoutes.LOGIN.route) {
                                        inclusive = true
                                    }
                                }
                            }

                            FirebaseResponse.Loading -> {
                                logInMessage = "Creating ..."
                                hasClick = true
                            }

                            FirebaseResponse.Empty -> {
                                logInMessage = "Creating ..."
                                hasClick = true
                            }
                        }
                    })
                }
            }
        })
    MainContainer(
        modifier = modifier,
        invokeSnackBar = {
            if (canShowSnackBar.first) {
                this.launch {
                    it.showSnackbar(
                        message = canShowSnackBar.second ?: "Error",
                        actionLabel = "Ok",
                    )
                }
            }
        },
        bottomBar = {
            runWithDeviceCompose(
                onAndroid = {
                    BottomAppBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                        ) {
                            AppButton(
                                modifier = Modifier.fillMaxWidth(.3f)
                                    .padding(start = MaterialTheme.spacing.medium),
                                innerPadding = MaterialTheme.spacing.small,
                                text = "Skip",
                                onClick = {
                                    navHostController.navigate(ExpanseSyncRoutes.AppScreens.route) {
                                        pref.saveBoolean(
                                            PrefKeys.IS_LOG_IN_SKIP, true
                                        )
                                        launchSingleTop = true
                                        popUpTo(ExpanseSyncRoutes.LOGIN.route) {
                                            inclusive = true
                                        }
                                    }
                                })
                            GoogleButton(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(end = MaterialTheme.spacing.medium),
                                innerPadding = MaterialTheme.spacing.small,
                                text = "Google",
                                loadingText = logInMessage,
                                hasClick = hasClick,
                                hasClickChange = { hasClick = it }) {

                            }
                        }
                    }
                })
        }) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(contentPadding),
        ) {
            Image(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(
                    if (com.atech.expensesync.utils.isAndroid()) 0.5f else 0.25f
                ).padding(
                    horizontal = MaterialTheme.spacing.large
                ),
                painter = painterResource(Res.drawable.login),
                contentDescription = "Money",
            )
            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
            Column(
                modifier = Modifier.fillMaxWidth().fillMaxHeight()
                    .padding(MaterialTheme.spacing.medium),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Set Your Budget With Expense Sync",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = if (com.atech.expensesync.utils.isAndroid()) TextAlign.Start else TextAlign.Center,
                )
                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
                Text(
                    text = "Set your budget and track your expenses in one place",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = if (com.atech.expensesync.utils.isAndroid()) TextAlign.Start else TextAlign.Center,
                )
                runWithDeviceCompose(
                    onDesktop = {
                        expenseSyncLogger(userState.toString())
                        if (userState != null && userState.uid.isNotEmpty()
                            && userState.systemUid != null
                            && userState.desktopLogInDetails != null
                            && userState.systemUid == pref.getString(PrefKeys.DESKTOP_USER_UID)
                        ) {
                            pref.saveString(
                                PrefKeys.USER_ID, userState.uid
                            )
                            pref.saveString(
                                PrefKeys.USER_MODEL, userState.toJson()
                            )
                            navHostController.navigate(LogInNavigation.BackUpScreen.route) {
                                launchSingleTop = true
                                popUpTo(ExpanseSyncRoutes.LOGIN.route) {
                                    inclusive = true
                                }
                            }
                            destroyViewModelObject.invoke()
                        }
                        com.atech.expensesync.utils.expenseSyncLogger(
                            "${pref.getString(PrefKeys.DESKTOP_USER_UID)}$${com.atech.expensesync.utils.getOsName()}"
                        )
                        onEvent.invoke(
                            LogInEvents.ObserveLogInData(
                                pref.getString(PrefKeys.DESKTOP_USER_UID)
                            )
                        )
                        QRComposable().generateContent(
                            "${pref.getString(PrefKeys.DESKTOP_USER_UID)}$${com.atech.expensesync.utils.getOsName()}"
                        ).invoke()
                    })
            }
        }
    }
}