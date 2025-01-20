package com.atech.expensesync.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.atech.expensesync.ui.compose.login.compose.LogInScreen
import com.atech.expensesync.utils.fadeThroughComposable


sealed class LogInNavigation(val routes: String) {
    object LogInScreen : LogInNavigation("login_screen")
}


fun NavGraphBuilder.logInScreenNavigation(
    navHostController: NavHostController
) {
    navigation(
        startDestination = LogInNavigation.LogInScreen.routes,
        route = "login"
    ) {
        fadeThroughComposable(
            route = LogInNavigation.LogInScreen.routes
        ) {
            LogInScreen(
                navHostController = navHostController
            )
        }
    }
}