package com.atech.expensesync.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.atech.expensesync.ui.screens.app.AppScreen
import com.atech.expensesync.ui.screens.scan.compose.ScanScreen
import com.atech.expensesync.ui.screens.split.add.compose.ViewExpanseBookScreen
import com.atech.expensesync.ui_utils.animatedComposable
import com.atech.expensesync.ui_utils.animatedComposableEnh
import kotlinx.serialization.Serializable

sealed class AppNavigation(val route: String) {
    object AppScreen : AppNavigation("app_screen")
    object ScanScreen : AppNavigation("scan_screen")
}

@Serializable
data class ViewExpanseBook(
    val grpName: String,
)

fun NavGraphBuilder.appNavigation(
    navHostController: NavHostController,
) {
    navigation(
        route = ExpanseSyncNavigation.AppScreens.route,
        startDestination = AppNavigation.AppScreen.route
    ) {
        animatedComposable(
            route = AppNavigation.AppScreen.route
        ) {
            AppScreen(
                navHostController = navHostController
            )
        }
        animatedComposable(
            route = AppNavigation.ScanScreen.route
        ) {
            ScanScreen(
                navHostController = navHostController
            )
        }
        animatedComposableEnh<ViewExpanseBook> {
            val state = it.toRoute<ViewExpanseBook>()
            ViewExpanseBookScreen(
                grpName = state.grpName,
                navHostController = navHostController
            )
        }
    }
}