package com.atech.expensesync.ui.screens.expense.root.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.atech.expensesync.LocalUploadDataHelper
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.room.expense.ExpenseBook
import com.atech.expensesync.firebase.util.FirebaseResponse
import com.atech.expensesync.firebase.util.getOrNull
import com.atech.expensesync.ui.screens.expense.detail.compose.CashInOutScreen
import com.atech.expensesync.ui.screens.expense.detail.compose.CashType
import com.atech.expensesync.ui.screens.expense.detail.compose.ExpenseDetailsScreen
import com.atech.expensesync.ui.screens.expense.root.ExpenseEvents
import com.atech.expensesync.ui.screens.expense.root.ExpenseViewModel
import com.atech.expensesync.ui.screens.expense.root.compose.add_edit.AddEditScreen
import com.atech.expensesync.ui.screens.meal.root.compose.handelFabState
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.backHandlerThreePane
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


internal enum class ExtraScreenType {
    ADD, CASH_IN_OUT
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, KoinExperimentalAPI::class)
@Composable
fun ExpenseScreen(
    modifier: Modifier = Modifier,
    canShowAppBar: (Boolean) -> Unit,
    navHostController: NavHostController
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    val lazyListState = rememberLazyListState()
    val viewModel = koinViewModel<ExpenseViewModel>()

    val expenseBooks by viewModel.expenseBooks.collectAsState(FirebaseResponse.Loading)
    var extraScreenType by remember { mutableStateOf(ExtraScreenType.ADD) }
    var cashInOutType by remember { mutableStateOf(CashType.CASH_IN) }
    val uploadDataHelper = LocalUploadDataHelper.current

    navigator.backHandlerThreePane(
        {
            if (extraScreenType == ExtraScreenType.ADD) viewModel.onEvent(
                ExpenseEvents.ResetStates
            )
        })
    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                canShowAppBar.invoke(true)
                MainScreen(
                    listState = lazyListState,
                    onAddMealBookClick = {
                        extraScreenType = ExtraScreenType.ADD
                        navigator.navigateTo(
                            ListDetailPaneScaffoldRole.Extra
                        )
                    },
                    expenseBook = expenseBooks.getOrNull() ?: emptyList(),
                    onMealBookItemClick = {
                        viewModel.onEvent(
                            ExpenseEvents.OnExpenseBookClick(
                                it
                            )
                        )
                        navigator.navigateTo(
                            ListDetailPaneScaffoldRole.Detail
                        )
                    }
                )
            }
        },
        detailPane = if (viewModel.clickedExpenseBook.value != null) {
            {
                AnimatedPane {
                    canShowAppBar.invoke(false)
                    ExpenseDetailsScreen(
                        state = viewModel.clickedExpenseBook.value!!,
                        expenseBookEntry = viewModel.getExpenseBookEntry.value,
                        onNavigateBack = {
                            viewModel.onEvent(
                                ExpenseEvents.ResetStates
                            )
                            navigator.navigateBack()
                        },
                        cashInOutClick = {
                            extraScreenType = ExtraScreenType.CASH_IN_OUT
                            cashInOutType = it
                            navigator.navigateTo(
                                ListDetailPaneScaffoldRole.Extra
                            )
                        },
                        onEvent = viewModel::onEvent
                    )
                }
            }
        } else {
            {}
        },
        extraPane = when (extraScreenType) {
            ExtraScreenType.ADD -> {
                {
                    AnimatedPane {
                        canShowAppBar.invoke(false)
                        AddEditScreen(
                            state = viewModel.expenseBook.value,
                            onEvent = viewModel::onEvent,
                            onNavigateClick = {
                                viewModel.onEvent(
                                    ExpenseEvents.ResetStates
                                )
                                navigator.navigateBack()
                            })
                    }
                }
            }

            ExtraScreenType.CASH_IN_OUT -> {
                {
                    AnimatedPane {
                        canShowAppBar.invoke(false)
                        CashInOutScreen(
                            bookId = viewModel.clickedExpenseBook.value?.bookId
                                ?: return@AnimatedPane,
                            cashType = cashInOutType,
                            onNavigationClick = {
                                navigator.navigateBack()
                            },
                            netBalance = viewModel.clickedExpenseBook.value?.netBalance ?: 0.0,
                            onSave = {
                                viewModel.onEvent(
                                    ExpenseEvents.OnSaveExpenseBookEntry(
                                        it, onComplete = {
                                            com.atech.expensesync.ui_utils.showToast(
                                                "Entry saved successfully",
                                            )
                                            uploadDataHelper.uploadExpenseData {
                                                navigator.navigateBack()
                                            }
                                        })
                                )
                            })
                    }
                }
            }
        })
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    expenseBook: List<ExpenseBook> = emptyList(),
    onAddMealBookClick: () -> Unit = {},
    onMealBookItemClick: (ExpenseBook) -> Unit = {}
) {
    MainContainer(
        modifier = modifier, title = "Expenses", floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add New Book") },
                expanded = listState.handelFabState(),
                icon = {
                    Icon(Icons.TwoTone.Add, contentDescription = "Add New Book")
                },
                onClick = onAddMealBookClick
            )
        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(MaterialTheme.spacing.medium),
            state = listState,
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(
                MaterialTheme.spacing.medium
            )
        ) {
            items(expenseBook) {
                ExpenseItem(
                    expenseBook = it, onClick = {
                        onMealBookItemClick(it)
                    })
            }
        }
    }
}

@Preview
@Composable
private fun ExpenseScreenPreview() {
    ExpenseSyncTheme {
        MainScreen()
    }
}