package com.atech.expensesync.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.atech.expensesync.ui.compose.home.compose.HomeScreen
import com.atech.expensesync.utils.fadeThroughComposable

sealed class AppNavigation(val route: String) {
    object Home : AppNavigation("home")
}

fun NavGraphBuilder.appNavigation(
    navHostController: NavHostController,
) {
    navigation(
        route = ExpanseSyncNavigation.AppScreens.route,
        startDestination = AppNavigation.Home.route
    ) {
        fadeThroughComposable(
            route = AppNavigation.Home.route
        ) {
            HomeScreen(
                navHostController = navHostController
            )
        }
    }
}