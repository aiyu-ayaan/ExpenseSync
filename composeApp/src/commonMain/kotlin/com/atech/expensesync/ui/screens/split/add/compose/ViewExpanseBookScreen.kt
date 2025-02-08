package com.atech.expensesync.ui.screens.split.add.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Groups
import androidx.compose.material.icons.twotone.Payment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.room.split.ExpanseGroupMembers
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.backHandlerThreePane

private enum class ExtraPane {
    AddExpense,
    GroupMembers,
    None
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ViewExpanseBookScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    grpName: String,
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    var extraPane by remember { mutableStateOf(ExtraPane.None) }
    navigator.backHandlerThreePane(
        backAction = {
            extraPane = ExtraPane.None
        }
    )
    ListDetailPaneScaffold(
        modifier = modifier.background(MaterialTheme.colorScheme.surface),
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
                        extraPane = ExtraPane.AddExpense
                        navigator.navigateTo(
                            ListDetailPaneScaffoldRole.Extra,
                        )
                    },
                    onGroupMembersClick = {
                        extraPane = ExtraPane.GroupMembers
                        navigator.navigateTo(
                            ListDetailPaneScaffoldRole.Extra,
                        )
                    }
                )
            }
        },
        detailPane = {

        },
        extraPane = when (extraPane) {
            ExtraPane.None -> {
                null
            }

            ExtraPane.AddExpense -> {
                {
                    AnimatedPane {
                        AddExpenseScreen(
                            grpName = grpName,
                            onNavigationClick = {
                                navigator.navigateBack()
                            }
                        )
                    }
                }
            }

            ExtraPane.GroupMembers -> {
                {
                    AnimatedPane {
                        GroupMembersScreen(
                            onNavigationClick = {
                                navigator.navigateBack()
                            },
                            state = listOf(
                                ExpanseGroupMembers(
                                    name = "Ayaan",
                                    email = "ayaan@gmail.com",
                                    pic = "https://picsum.photos/200",
                                    uid = "1",
                                    groupId = "1"
                                ),
                                ExpanseGroupMembers(
                                    name = "Anshu",
                                    email = "anshu@gmail.com",
                                    pic = "https://picsum.photos/200",
                                    uid = "2",
                                    groupId = "3"
                                )
                            )
                        )
                    }
                }
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
    onAddExpenseClick: () -> Unit = {},
    onGroupMembersClick: () -> Unit = {}
) {
    MainContainer(
        modifier = modifier,
        title = grpName,
        onNavigationClick = onNavigationClick,
        actions = {
            IconButton(onClick = onGroupMembersClick) {
                Icon(imageVector = Icons.TwoTone.Groups, contentDescription = "")
            }
        },
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
            SetTabLayout(
                state = state,
            ) {
                createTabs(
                    state = state,
                    stateChange = { state = it },
                    list = titles
                )
            }
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "Fancy indicator tab ${state + 1} selected",
                style = MaterialTheme.typography.bodyLarge
            )
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
    if (com.atech.expensesync.ui_utils.getDisplayType() ==
        com.atech.expensesync.ui_utils.DeviceType.MOBILE
    ) {
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
    }
}

@Composable
private fun createTabs(
    state: Int,
    stateChange: (Int) -> Unit,
    list: List<String>
) {
    list.forEachIndexed { index, title ->
        Tab(
            selected = state == index,
            onClick = { stateChange(index) },
            text = { Text(title) })
    }
}

