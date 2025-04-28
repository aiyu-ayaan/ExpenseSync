package com.atech.expensesync.ui.screens.splitv2.details.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Payment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.room.splitv2.SplitModel
import com.atech.expensesync.ui.screens.splitv2.details.SplitDetailsEvents
import com.atech.expensesync.ui.screens.splitv2.details.SplitDetailsViewModel
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.backHandlerThreePane
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


private enum class ExtraPane {
    AddExpense, GroupMembers, None, ShowDetailOfTransaction
}

private enum class TabState {
    SettleUp, Balance, Whiteboard, Export
}


@OptIn(ExperimentalMaterial3AdaptiveApi::class, KoinExperimentalAPI::class)
@Composable
fun SplitDetailsScreen(
    modifier: Modifier = Modifier,
    state: SplitModel,
    navHostController: NavHostController
) {
    val viewModel = koinViewModel<SplitDetailsViewModel>()
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    navigator.backHandlerThreePane()
    LaunchedEffect(state) {
        viewModel.onEvent(SplitDetailsEvents.SetSplitModel(state))
    }
    val detailsModel by viewModel.detailsModel
    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            BaseScreen(
                modifier = Modifier.fillMaxWidth(),
                onNavigateBack = {
                    navHostController.navigateUp()
                },
            )
        },
        detailPane = {

        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BaseScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
) {
    MainContainer(
        modifier = modifier,
        title = "Details",
        onNavigationClick = onNavigateBack,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
//                    TODO : Add Expense
                },
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.TwoTone.Payment, contentDescription = "Add")
                    Text(
                        modifier = Modifier.padding(start = MaterialTheme.spacing.medium),
                        text = "Add Expense"
                    )
                }
            }
        }
    ) { contentPadding ->
        var state by remember { mutableStateOf(0) }
        Column(
            modifier = Modifier.padding(contentPadding),
        ) {
            SetTabLayout(
                state = state,
            ) {
                CreateTabs(
                    state = state,
                    stateChange = { state = it },
                    list = TabState.entries.map { it.name })
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SetTabLayout(
    modifier: Modifier = Modifier,
    state: Int,
    tabs: @Composable (() -> Unit),
) {
    if (com.atech.expensesync.ui_utils.getDisplayType() == com.atech.expensesync.ui_utils.DeviceType.MOBILE) {
        ScrollableTabRow(
            modifier = modifier.fillMaxWidth(),
            selectedTabIndex = state,
        ) {
            tabs()
        }
    } else {
        PrimaryTabRow(
            selectedTabIndex = state,
        ) {
            tabs()
        }
        when (TabState.entries[state]) {
            TabState.SettleUp -> {
                Text(
                    text = "Settle Up",
                    modifier = Modifier.padding(MaterialTheme.spacing.medium)
                )
            }

            TabState.Balance -> {
                Text(text = "Balance")
            }

            TabState.Whiteboard -> {
                Text(text = "Whiteboard")
            }

            TabState.Export -> {
                Text(text = "Export")
            }
        }
    }
}

@Composable
private fun CreateTabs(
    state: Int, stateChange: (Int) -> Unit, list: List<String>
) {
    list.forEachIndexed { index, title ->
        Tab(selected = state == index, onClick = { stateChange(index) }, text = { Text(title) })
    }
}