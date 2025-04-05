package com.atech.expensesync.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import org.koin.core.scope.Scope


enum class ExpanseSyncRoutes(val route: String) {
    LOGIN("login"),
    AppScreens("app_screens")
}

sealed class ExpanseSyncNavigation(
    val route: String
) {
    object LogInScreen : ExpanseSyncNavigation(ExpanseSyncRoutes.LOGIN.route)
    object AppScreens : ExpanseSyncNavigation(ExpanseSyncRoutes.AppScreens.route)
}


@Composable
fun ExpanseSyncNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    startDestination: String = ExpanseSyncNavigation.LogInScreen.route
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = startDestination
    ) {
        logInScreenNavigation(navHostController)
        appNavigation(
            navHostController,
        )
    }
}