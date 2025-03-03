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
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.room.split.SplitGroupMembers
import com.atech.expensesync.database.room.split.SplitTransactions
import com.atech.expensesync.database.room.split.TransactionSplit
import com.atech.expensesync.ui.screens.split.add.AddExpenseEvents
import com.atech.expensesync.ui.screens.split.add.CreateExpenseState
import com.atech.expensesync.ui.screens.split.add.ViewExpenseBookState
import com.atech.expensesync.ui.screens.split.add.compose.settleUpScreen.SettleUpScreen
import com.atech.expensesync.ui.screens.split.add.compose.settleUpScreen.ShowDetailOfTransaction
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.backHandlerThreePane

private enum class ExtraPane {
    AddExpense, GroupMembers, None, ShowDetailOfTransaction
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ViewExpanseBookScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    state: ViewExpenseBookState,
    addExpenseBookState: CreateExpenseState,
    members: List<SplitGroupMembers>,
    transactionWithUser: State<Map<SplitTransactions, List<Pair<TransactionSplit, SplitGroupMembers>>>>,
    onEvent: (AddExpenseEvents) -> Unit
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    var extraPane by remember { mutableStateOf(ExtraPane.None) }
//    groupMembers: List<ExpanseGroupMembers>,
//    split: List<TransactionSplit>,

    var clickedTranslation by remember { mutableStateOf<SplitTransactions?>(null) }
    var clickedGroupMember by remember { mutableStateOf<List<SplitGroupMembers>?>(null) }
    var clickedTransactionSplit by remember { mutableStateOf<List<TransactionSplit>?>(null) }


    navigator.backHandlerThreePane(
        backAction = {
            extraPane = ExtraPane.None
        })
    ListDetailPaneScaffold(
        modifier = modifier.background(MaterialTheme.colorScheme.surface),
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                Screen(
                    grpName = state.groupName,
                    onNavigationClick = {
                        navHostController.navigateUp()
                    },
                    transactionWithUser = transactionWithUser,
                    onAddExpenseClick = {
                        extraPane = ExtraPane.AddExpense
                        onEvent(AddExpenseEvents.OnCreateExpenseStateReset)
                        navigator.navigateTo(
                            ListDetailPaneScaffoldRole.Extra,
                        )
                    },
                    onGroupMembersClick = {
                        extraPane = ExtraPane.GroupMembers
                        onEvent(AddExpenseEvents.GetExpenseGroupMembers)
                        navigator.navigateTo(
                            ListDetailPaneScaffoldRole.Extra,
                        )
                    },
                    onEvent = onEvent,
                    members = members,
                    navigator = navigator,
                    onExtraPaneChange = {
                        extraPane = it
                    },
                    onItemClick = { expanseTransaction, groupMembers, split ->
                        clickedTranslation = expanseTransaction
                        clickedGroupMember = groupMembers
                        clickedTransactionSplit = split
                        extraPane = ExtraPane.ShowDetailOfTransaction
                        navigator.navigateTo(
                            ListDetailPaneScaffoldRole.Extra,
                        )
                    })
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
                            title = if (addExpenseBookState.transactionId.isBlank()) AddExpenseScreenTitle.ADD
                            else AddExpenseScreenTitle.EDIT,
                            viewExpenseBookState = state,
                            state = addExpenseBookState,
                            groupMembers = members,
                            onEvent = onEvent,
                            onNavigationClick = {
                                navigator.navigateBack()
                            })
                    }
                }
            }

            ExtraPane.GroupMembers -> {
                {
                    AnimatedPane {
                        GroupMembersScreen(
                            onNavigationClick = {
                                navigator.navigateBack()
                            }, state = members, onEvent = onEvent
                        )
                    }
                }
            }

            ExtraPane.ShowDetailOfTransaction -> {
                {
                    AnimatedPane {
                        if (clickedTranslation != null && clickedGroupMember != null && clickedTransactionSplit != null) {
                            ShowDetailOfTransaction(
                                transaction = clickedTranslation!!,
                                groupMembers = clickedGroupMember!!,
                                split = clickedTransactionSplit!!,
                                onNavigationClick = {
                                    navigator.navigateBack()
                                })
                        }
                    }
                }
            }
        })
}


private enum class TabState {
    SettleUp, Balance, Whiteboard, Export
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun Screen(
    modifier: Modifier = Modifier,
    grpName: String,
    navigator: ThreePaneScaffoldNavigator<Nothing>,
    onExtraPaneChange: (ExtraPane) -> Unit,
    transactionWithUser: State<Map<SplitTransactions, List<Pair<TransactionSplit, SplitGroupMembers>>>>,
    members: List<SplitGroupMembers>,
    onNavigationClick: () -> Unit = {},
    onAddExpenseClick: () -> Unit = {},
    onGroupMembersClick: () -> Unit = {},
    onItemClick: (
        transaction: SplitTransactions,
        groupMembers: List<SplitGroupMembers>,
        split: List<TransactionSplit>,
    ) -> Unit,
    onEvent: (AddExpenseEvents) -> Unit
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
        }) { paddingValue ->
        var state by remember { mutableStateOf(0) }
        Column(
            modifier = Modifier.padding(paddingValue),
        ) {
            SetTabLayout(
                state = state,
            ) {
                createTabs(
                    state = state,
                    stateChange = { state = it },
                    list = TabState.entries.map { it.name })
            }
            when (TabState.entries[state]) {
                TabState.SettleUp -> {
                    onEvent(AddExpenseEvents.LoadSettleUpScreen)
                    SettleUpScreen(
                        state = transactionWithUser.value,
                        groupMembers = members,
                        onClick = onItemClick,
                        onEditClick = {
                            onExtraPaneChange.invoke(ExtraPane.AddExpense)
                            onEvent(AddExpenseEvents.SetTransactionForEdit(it))
                            navigator.navigateTo(
                                ListDetailPaneScaffoldRole.Extra,
                            )
                        })
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
    }
}

@Composable
private fun createTabs(
    state: Int, stateChange: (Int) -> Unit, list: List<String>
) {
    list.forEachIndexed { index, title ->
        Tab(selected = state == index, onClick = { stateChange(index) }, text = { Text(title) })
    }
}

