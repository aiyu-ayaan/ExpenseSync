package com.atech.expensesync.navigation

import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.atech.expensesync.ui.screens.app.AppScreen
import com.atech.expensesync.ui.screens.meal.root.AddMealBookState
import com.atech.expensesync.ui.screens.meal.view.ViewMealEvents
import com.atech.expensesync.ui.screens.meal.view.ViewMealViewModel
import com.atech.expensesync.ui.screens.meal.view.compose.ViewMealScreen
import com.atech.expensesync.ui.screens.scan.compose.ScanScreen
import com.atech.expensesync.ui.screens.split.add.AddExpenseEvents
import com.atech.expensesync.ui.screens.split.add.AddExpenseViewModel
import com.atech.expensesync.ui.screens.split.add.compose.ViewExpanseBookScreen
import com.atech.expensesync.ui_utils.animatedComposable
import com.atech.expensesync.ui_utils.animatedComposableEnh
import com.atech.expensesync.ui_utils.koinViewModel
import com.atech.expensesync.utils.Currency
import kotlinx.serialization.Serializable

sealed class AppNavigation(val route: String) {
    data object AppScreen : AppNavigation("app_screen")
    data object ScanScreen : AppNavigation("scan_screen")
}

@Serializable
data class ViewExpanseBookArgs(
    val grpId: String,
    val grpName: String,
)

@Serializable
data class ViewMealArgs(
    val name: String,
    val defaultPrice: String,
    val description: String,
    val defaultCurrency: String,
    val mealBookId: String,
    val createdAt: String
)

fun ViewMealArgs.toAddMealBookState() = AddMealBookState(
    name = name,
    defaultPrice = defaultPrice.toDoubleOrNull() ?: 0.0,
    description = description,
    defaultCurrency = Currency.valueOf(defaultCurrency),
    mealBookId = mealBookId,
    createdAt = createdAt.toLong()
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
            val viewModel = koinViewModel<AddExpenseViewModel>()
            viewModel.onEvent(AddExpenseEvents.SetViewExpenseBookArgs(args))
            ViewExpanseBookScreen(
                navHostController = navHostController,
                state = viewModel.viewExpenseBookState.value,
                addExpenseBookState = viewModel.createExpenseState.value,
                members = viewModel.grpMembers.value,
                transactionWithUser = viewModel.getTransactionWithUser,
                onEvent = viewModel::onEvent
            )
        }
        animatedComposableEnh<ViewMealArgs> {
            val args = it.toRoute<ViewMealArgs>().toAddMealBookState()
            val viewModel = koinViewModel<ViewMealViewModel>()
            viewModel.onEvent(ViewMealEvents.SetMealBookId(args.mealBookId, args))
            val calenderMonth by viewModel.calenderMonth
            val mealBookEntryState by viewModel.mealBookEntryState
            ViewMealScreen(
                mealBookState = viewModel.mealBookState.value ?: args,
                state = mealBookEntryState,
                calenderMonth = calenderMonth,
                onEvent = viewModel::onEvent,
                onNavigateUp = {
                    navHostController.navigateUp()
                }
            )
        }
    }
}