package com.atech.expensesync.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.atech.expensesync.ui.screens.login.LogInViewModel
import com.atech.expensesync.ui.screens.login.compose.LogInScreen
import com.atech.expensesync.ui_utils.fadeThroughComposable
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


sealed class LogInNavigation(val routes: String) {
    data object LogInScreen : LogInNavigation("login_screen")
}


@OptIn(KoinExperimentalAPI::class)
fun NavGraphBuilder.logInScreenNavigation(
    navHostController: NavHostController,
) {
    navigation(
        startDestination = LogInNavigation.LogInScreen.routes,
        route = "login"
    ) {
        fadeThroughComposable(
            route = LogInNavigation.LogInScreen.routes
        ) {
            val viewModel = koinViewModel<LogInViewModel>()
            LogInScreen(
                navHostController = navHostController,
                onEvent = viewModel::onEvent,
                userState = viewModel.user.value,
                destroyViewModelObject = {
                }
            )
        }
    }
}