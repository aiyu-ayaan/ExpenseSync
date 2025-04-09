package com.atech.expensesync.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.atech.expensesync.ui.screens.backup.compose.BackUpScreen
import com.atech.expensesync.ui.screens.login.LogInViewModel
import com.atech.expensesync.ui.screens.login.compose.LogInScreen
import com.atech.expensesync.ui_utils.fadeThroughComposable
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


sealed class LogInNavigation(val route: String) {
    data object LogInScreen : LogInNavigation("login_screen")
    data object BackUpScreen : LogInNavigation("backup_screen")
}


@OptIn(KoinExperimentalAPI::class)
fun NavGraphBuilder.logInScreenNavigation(
    navHostController: NavHostController,
) {
    navigation(
        startDestination = LogInNavigation.LogInScreen.route,
        route = "login"
    ) {
        fadeThroughComposable(
            route = LogInNavigation.LogInScreen.route
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
        fadeThroughComposable(
            route = LogInNavigation.BackUpScreen.route
        ) {
            BackUpScreen(
                navHostController = navHostController,
            )
        }
    }
}