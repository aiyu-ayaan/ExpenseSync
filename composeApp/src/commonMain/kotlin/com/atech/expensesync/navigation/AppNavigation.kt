package com.atech.expensesync.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.atech.expensesync.ui.compose.app.AppScreen
import com.atech.expensesync.utils.fadeThroughComposable

sealed class AppNavigation(val route: String) {
    object AppScreen : AppNavigation("app_screen")
}

fun NavGraphBuilder.appNavigation(
    navHostController: NavHostController,
) {
    navigation(
        route = ExpanseSyncNavigation.AppScreens.route,
        startDestination = AppNavigation.AppScreen.route
    ) {
        fadeThroughComposable(
            route = AppNavigation.AppScreen.route
        ) {
            AppScreen(
                navHostController = navHostController
            )
        }
    }
}