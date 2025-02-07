package com.atech.expensesync.ui.screens.split.add.compose

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
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.backHandlerThreePane


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ViewExpanseBookScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    grpName: String,
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    navigator.backHandlerThreePane()
    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                Screen(
                    grpName = grpName,
                    onNavigationClick = {
                        navHostController.navigateUp()
                    },
                    onAddExpenseClick = {
                        navigator.navigateTo(
                            ListDetailPaneScaffoldRole.Extra,
                        )
                    }
                )
            }
        },
        detailPane = {

        },
        extraPane = {
            AnimatedPane {
                AddExpenseScreen(
                    grpName = grpName,
                    onNavigationClick = {
                        navigator.navigateBack()
                    }
                )
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Screen(
    modifier: Modifier = Modifier,
    grpName: String,
    onNavigationClick: () -> Unit = {},
    onAddExpenseClick: () -> Unit = {}
) {
    MainContainer(
        modifier = modifier,
        title = grpName,
        onNavigationClick = onNavigationClick,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddExpenseClick,
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
    ) { paddingValue ->
        var state by remember { mutableIntStateOf(0) }
        val titles = listOf(
            "Settle Up",
            "Balance",
            "Whiteboard",
            "Export"
        )
        Column(
            modifier = Modifier.padding(paddingValue),
        ) {
            ScrollableTabRow(
                modifier = Modifier.fillMaxWidth(),
                selectedTabIndex = state,
            ) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        selected = state == index,
                        onClick = { state = index },
                        text = { Text(title) })
                }
            }
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "Fancy indicator tab ${state + 1} selected",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

