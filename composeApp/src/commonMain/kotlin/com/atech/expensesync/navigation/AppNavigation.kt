package com.atech.expensesync.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.atech.expensesync.database.room.splitv2.SplitModel
import com.atech.expensesync.database.room.splitv2.Type
import com.atech.expensesync.ui.screens.app.AppScreen
import com.atech.expensesync.ui.screens.scan.compose.ScanScreen
import com.atech.expensesync.ui.screens.splitv2.details.compose.SplitDetailsScreen
import com.atech.expensesync.ui_utils.animatedComposable
import com.atech.expensesync.ui_utils.animatedComposableEnh
import com.atech.expensesync.utils.Currency
import kotlinx.serialization.Serializable
import org.koin.core.annotation.KoinExperimentalAPI

sealed class AppNavigation(val route: String) {
    data object AppScreen : AppNavigation("app_screen")
    data object ScanScreen : AppNavigation("scan_screen")
}

@Serializable
data class ViewSplitBookArgs(
    val groupName: String,
    val createdByUid: String,
    val defaultCurrency: Currency = Currency.INR,
    val groupType: Type = Type.None,
    val isActive: Boolean = true,
    val whiteBoard: String? = null,
    val createdAt: Long,
    val groupId: String,
)

fun ViewSplitBookArgs.toSplitModel(): SplitModel = SplitModel(
    groupName = groupName,
    createdByUid = createdByUid,
    defaultCurrency = defaultCurrency,
    groupType = groupType,
    isActive = isActive,
    whiteBoard = whiteBoard,
    createdAt = createdAt,
    groupId = groupId
)

fun SplitModel.toViewSplitBookArgs(): ViewSplitBookArgs = ViewSplitBookArgs(
    groupName = groupName,
    createdByUid = createdByUid,
    defaultCurrency = defaultCurrency,
    groupType = groupType,
    isActive = isActive,
    whiteBoard = whiteBoard,
    createdAt = createdAt,
    groupId = groupId
)


@OptIn(KoinExperimentalAPI::class)
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
        animatedComposableEnh<ViewSplitBookArgs> {
            val item = it.toRoute<ViewSplitBookArgs>().toSplitModel()
            SplitDetailsScreen(
                state = item,
                navHostController = navHostController
            )
        }
    }
}