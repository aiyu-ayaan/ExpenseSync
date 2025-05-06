package com.atech.expensesync.ui.screens.splitv2.details.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavHostController
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.models.GroupMember
import com.atech.expensesync.database.models.SplitTransaction
import com.atech.expensesync.database.models.TransactionGlobalModel
import com.atech.expensesync.firebase.util.getOrNull
import com.atech.expensesync.firebase.util.isSuccess
import com.atech.expensesync.navigation.ViewSplitBookArgs
import com.atech.expensesync.ui.screens.meal.root.compose.handelFabState
import com.atech.expensesync.ui.screens.splitv2.details.SplitDetailsEvents
import com.atech.expensesync.ui.screens.splitv2.details.SplitDetailsEvents.SetSplitTransaction
import com.atech.expensesync.ui.screens.splitv2.details.SplitDetailsViewModel
import com.atech.expensesync.ui.screens.splitv2.details.compose.settleUp.SettleMoneyScreen
import com.atech.expensesync.ui.screens.splitv2.details.compose.settleUp.SettleUpScreen
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.backHandlerThreePane
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


private enum class ExtraPane {
    AddExpense, GroupMembers, None, ShowDetailOfTransaction, SETTLE_UP
}

private enum class TabState {
    SettleUp, Balance, Whiteboard, Export
}


@OptIn(ExperimentalMaterial3AdaptiveApi::class, KoinExperimentalAPI::class)
@Composable
fun SplitDetailsScreen(
    modifier: Modifier = Modifier, args: ViewSplitBookArgs, navHostController: NavHostController
) {
    val viewModel = koinViewModel<SplitDetailsViewModel>()
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    navigator.backHandlerThreePane()
    var extraPane by remember { mutableStateOf(ExtraPane.None) }
    LaunchedEffect(args) {
        viewModel.onEvent(SplitDetailsEvents.SetId(args.id))
    }
    val splitDetails by viewModel.splitDetails
    var settleUpScreenState by remember { mutableStateOf(Triple("", "", 0.0)) }

    if (splitDetails.isSuccess()) {
        val groupMembers = splitDetails.getOrNull()?.members ?: return
        val adminUid = splitDetails.getOrNull()?.createdByUid ?: return
        val groupName = splitDetails.getOrNull()?.groupName ?: return
        val globalTransaction = viewModel.globalTransactionDetails.value
        val splitTransactions = viewModel.splitTransactions.value
        ListDetailPaneScaffold(
            modifier = modifier.background(
                MaterialTheme.colorScheme.surface
            ),
            directive = navigator.scaffoldDirective,
            value = navigator.scaffoldValue,
            listPane = {
                AnimatedPane {
                    BaseScreen(
                        modifier = Modifier.fillMaxWidth(),
                        groupMembers = groupMembers,
                        globalTransaction = globalTransaction,
                        splitTransactions = splitTransactions,
                        onNavigateBack = {
                            navHostController.navigateUp()
                        },
                        onGroupMembersClick = {
                            extraPane = ExtraPane.GroupMembers
                            navigator.navigateTo(
                                ListDetailPaneScaffoldRole.Extra
                            )
                        },
                        navigateToAddExpense = {
                            extraPane = ExtraPane.AddExpense
                            navigator.navigateTo(
                                ListDetailPaneScaffoldRole.Extra
                            )
                        },
                        onSettleUpClick = { debtorUid, creditorUid, amount ->
                            settleUpScreenState = Triple(debtorUid, creditorUid, amount)
                            extraPane = ExtraPane.SETTLE_UP
                            navigator.navigateTo(
                                ListDetailPaneScaffoldRole.Extra
                            )
                        }
                    )
                }
            },
            detailPane = {

            },
            extraPane = when (extraPane) {
                ExtraPane.AddExpense -> {
                    {
                        AnimatedPane {
                            AddExpenseScreen(
                                modifier = Modifier.fillMaxWidth(),
                                state = viewModel.splitTransaction.value,
                                name = groupName,
                                groupMembers = groupMembers,
                                onEvent = viewModel::onEvent,
                                onNavigationClick = {
                                    viewModel.onEvent(
                                        SetSplitTransaction()
                                    )
                                    extraPane = ExtraPane.None
                                    navigator.navigateBack()
                                })
                        }
                    }
                }

                ExtraPane.GroupMembers -> {
                    {
                        AnimatedPane {
                            AddMemberScreen(
                                modifier = Modifier.fillMaxWidth(),
                                state = groupMembers,
                                adminUid = adminUid,
                                onEvent = viewModel::onEvent,
                                onNavigationClick = {
                                    extraPane = ExtraPane.None
                                    navigator.navigateBack()
                                })
                        }
                    }
                }

                ExtraPane.None -> null
                ExtraPane.ShowDetailOfTransaction -> {
                    {}
                }

                ExtraPane.SETTLE_UP -> {
                    {
                        SettleMoneyScreen(
                            members = groupMembers,
                            triple = settleUpScreenState,
                            onNavigateBack = {
                                extraPane = ExtraPane.None
                                navigator.navigateBack()
                            },
                            onEvent = viewModel::onEvent
                        )
                    }
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BaseScreen(
    modifier: Modifier = Modifier,
    groupMembers: List<GroupMember>,
    globalTransaction: List<TransactionGlobalModel>,
    splitTransactions: List<SplitTransaction>,
    onSettleUpClick: (debtorUid: String, creditorUid: String, amount: Double) -> Unit = { _, _, _ -> },
    onGroupMembersClick: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    navigateToAddExpense: () -> Unit = {},
) {
    val listState = LazyListState()
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    MainContainer(
        modifier = modifier
            .nestedScroll(
                topAppBarScrollBehavior.nestedScrollConnection
            ),
        scrollBehavior = topAppBarScrollBehavior,
        title = "Details",
        onNavigationClick = onNavigateBack,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navigateToAddExpense.invoke()
                },
                expanded = listState.handelFabState(),
                text = {
                    Text(
                        modifier = Modifier.padding(start = MaterialTheme.spacing.medium),
                        text = "Add Expense"
                    )
                },
                icon = {
                    Icon(imageVector = Icons.TwoTone.Payment, contentDescription = "Add")
                }
            )
        },
        actions = {
            IconButton(onClick = onGroupMembersClick) {
                Icon(imageVector = Icons.TwoTone.Groups, contentDescription = "")
            }
        }) { contentPadding ->
        var state by remember { mutableIntStateOf(0) }
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
            when (TabState.entries[state]) {
                TabState.SettleUp -> SettleUpScreen(
                    globalTransaction = globalTransaction,
                    listState = listState,
                    groupMembers = groupMembers,
                    splitTransactions = splitTransactions,
                    onSettleUpClick = onSettleUpClick
                )

                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text("TODO():Implemented")
                    }
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
        when (TabState.entries[state]) {
            TabState.SettleUp -> {
                Text(
                    text = "Settle Up", modifier = Modifier.padding(MaterialTheme.spacing.medium)
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