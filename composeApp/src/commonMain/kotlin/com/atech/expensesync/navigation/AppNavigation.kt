package com.atech.expensesync.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.atech.expensesync.ui.screens.app.AppScreen
import com.atech.expensesync.ui.screens.scan.compose.ScanScreen
import com.atech.expensesync.ui.screens.split.add.AddExpanseEvents
import com.atech.expensesync.ui.screens.split.add.AddExpanseViewModel
import com.atech.expensesync.ui.screens.split.add.compose.ViewExpanseBookScreen
import com.atech.expensesync.ui_utils.animatedComposable
import com.atech.expensesync.ui_utils.animatedComposableEnh
import com.atech.expensesync.ui_utils.koinViewModel
import kotlinx.serialization.Serializable

sealed class AppNavigation(val route: String) {
    object AppScreen : AppNavigation("app_screen")
    object ScanScreen : AppNavigation("scan_screen")
}

@Serializable
data class ViewExpanseBookArgs(
    val grpId: String,
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
        animatedComposableEnh<ViewExpanseBookArgs> {
            val args = it.toRoute<ViewExpanseBookArgs>()
            val viewModel = koinViewModel<AddExpanseViewModel>()
            viewModel.onEvent(AddExpanseEvents.SetViewExpanseBookArgs(args))
            ViewExpanseBookScreen(
                state = viewModel.addExpenseState.value,
                members = viewModel.grpMembers.value,
                navHostController = navHostController,
                onEvent = viewModel::onEvent
            )
        }
    }
}