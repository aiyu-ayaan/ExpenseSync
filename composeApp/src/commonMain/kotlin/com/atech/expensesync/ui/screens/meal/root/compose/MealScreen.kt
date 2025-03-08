package com.atech.expensesync.ui.screens.meal.root.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Book
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavHostController
import com.atech.expensesync.component.BottomPadding
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.room.meal.MealBook
import com.atech.expensesync.navigation.ViewMealArgs
import com.atech.expensesync.navigation.toAddMealBookState
import com.atech.expensesync.ui.screens.meal.edit.EditMealBookDialog
import com.atech.expensesync.ui.screens.meal.edit.EditMealDialog
import com.atech.expensesync.ui.screens.meal.root.AddMealBookState
import com.atech.expensesync.ui.screens.meal.root.MealScreenEvents
import com.atech.expensesync.ui.screens.meal.root.MealViewModel
import com.atech.expensesync.ui.screens.meal.root.compose.add.AddMealBookScreen
import com.atech.expensesync.ui.screens.meal.view.ViewMealEvents
import com.atech.expensesync.ui.screens.meal.view.ViewMealViewModel
import com.atech.expensesync.ui.screens.meal.view.compose.ViewMealScreen
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.backHandlerThreePane
import com.atech.expensesync.ui_utils.formatAmount
import com.atech.expensesync.ui_utils.showToast
import com.atech.expensesync.utils.Currency
import com.atech.expensesync.utils.checkIts1stDayOfMonth
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

//private enum class DetailsScreenType {
//    AddMealBook, None
//}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, KoinExperimentalAPI::class)
@Composable
fun MealScreen(
    modifier: Modifier = Modifier,
    canShowAppBar: (Boolean) -> Unit,
    viewModel: MealViewModel = koinViewModel(),
    navHostController: NavHostController
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    val mealViewModel = koinViewModel<ViewMealViewModel>()
    val mealBookItems by viewModel.mealBooks.collectAsState(emptyList())

    val lazyListState = rememberLazyListState()
    navigator.backHandlerThreePane(backAction = {
        viewModel.onEvent(MealScreenEvents.OnMealScreenStateChange(null))
    })

    var viewMealArgs: ViewMealArgs? by remember { mutableStateOf(null) }
    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                canShowAppBar.invoke(true)
                MealListScreen(
                    listState = lazyListState,
                    onDeleteClear = {
                        viewMealArgs = null
                    },
                    onItemClick = {
                        /*navHostController.navigate(*/
                        viewMealArgs = ViewMealArgs(
                            mealBookId = it.mealBookId,
                            name = it.name,
                            defaultPrice = it.defaultPrice.toString(),
                            defaultCurrency = it.defaultCurrency.name,
                            description = it.description,
                            createdAt = it.created.toString()
                        )
                        navigator.navigateTo(
                            ListDetailPaneScaffoldRole.Detail
                        )
//                    )
                    }, onAddMealBookClick = {
                        viewModel.onEvent(MealScreenEvents.OnMealScreenStateChange(AddMealBookState()))
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Extra)
                    }, state = mealBookItems, onEvent = viewModel::onEvent, calculateTotalPrice = {
                        val value by viewModel.calculateTotalForCurrentMonth(it).collectAsState(0.0)
                        value
                    }, calculateTotalLastMonthPrice = {
                        if (checkIts1stDayOfMonth()) {
                            val value by viewModel.calculateTotalForLastMonth(it)
                                .collectAsState(0.0)
                            value
                        } else 0.0
                    })
            }
        },
        extraPane = {
            AnimatedPane {
                canShowAppBar.invoke(false)
                AddMealBookScreen(
                    state = viewModel.addMealState.value ?: return@AnimatedPane,
                    onEvent = viewModel::onEvent,
                    onNavigationClick = {
                        viewModel.onEvent(MealScreenEvents.OnMealScreenStateChange(null))
                        navigator.navigateBack()
//                        detailsScreenType = DetailsScreenType.None
                    })
            }
        },
        detailPane = {
            if (viewMealArgs == null) return@ListDetailPaneScaffold
            AnimatedPane {
                canShowAppBar.invoke(false)
                val arg = viewMealArgs!!.toAddMealBookState()
                mealViewModel.onEvent(
                    ViewMealEvents.SetMealBookId(
                        viewMealArgs!!.mealBookId,
                        arg
                    )
                )
                val calenderMonth by mealViewModel.calenderMonth
                val mealBookEntryState by mealViewModel.mealBookEntryState
                ViewMealScreen(
                    mealBookState = mealViewModel.mealBookState.value
                        ?: arg,
                    state = mealBookEntryState,
                    calenderMonth = calenderMonth,
                    onEvent = mealViewModel::onEvent,
                    onNavigateUp = {
                        navigator.navigateBack()
                        viewMealArgs = null
                    },
                    onDeleteClear = {
                        viewMealArgs = null
                        navigator.navigateBack()
                    }
                )
            }
        }
    )
}

@Composable
fun LazyListState.handelFabState(): Boolean {
    val firstVisibleItemIndex by remember { derivedStateOf { this.firstVisibleItemIndex } }
    val firstItemScrollOffset by remember { derivedStateOf { this.firstVisibleItemScrollOffset } }
    val isFabExpanded by remember {
        derivedStateOf {
            // Expanded when at the top of the list
            firstVisibleItemIndex == 0 && firstItemScrollOffset == 0
        }
    }
    return isFabExpanded
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MealListScreen(
    modifier: Modifier = Modifier,
    onAddMealBookClick: () -> Unit = {},
    state: List<MealBook>,
    listState: LazyListState,
    onDeleteClear: () -> Unit = {},
    calculateTotalPrice: @Composable (String) -> Double,
    calculateTotalLastMonthPrice: @Composable (String) -> Double = { 0.0 },
    onEvent: (MealScreenEvents) -> Unit = {},
    onItemClick: (MealBook) -> Unit = {}
) {
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()


    var isPriceDialogVisible by remember { mutableStateOf(false) }
    var price by remember { mutableStateOf(0.0.formatAmount()) }
    var currency by remember { mutableStateOf(Currency.INR) }
    var mealBookId: String? by remember { mutableStateOf(null) }
    var yetEditModel: AddMealBookState? by remember { mutableStateOf(null) }
    var isEditDialogVisible by remember { mutableStateOf(false) }

    AnimatedVisibility(isEditDialogVisible) {
        if (yetEditModel == null) return@AnimatedVisibility
        EditMealDialog(
            mealBook = yetEditModel!!,
            onDismissRequest = {
                isEditDialogVisible = false
                yetEditModel = null
            },
            onDeleteItem = {
                onDeleteClear.invoke()
                onEvent.invoke(
                    MealScreenEvents.DeleteMealBook(
                        mealBookId = yetEditModel!!.mealBookId,
                        onComplete = {
                            showToast("Meal Book deleted successfully")
                            isEditDialogVisible = false
                            yetEditModel = null
                        }
                    ))
            },
            confirmButton = {
                onEvent.invoke(MealScreenEvents.UpdateMealBook(it) { res ->
                    if (res < 0) {
                        showToast("Failed to create Meal Book.Check Meal Book name and try again")
                        return@UpdateMealBook
                    }
                    showToast("Meal Book updated successfully")
                    isEditDialogVisible = false
                    yetEditModel = null

                })
            }
        )
    }

    AnimatedVisibility(isPriceDialogVisible) {
        EditMealBookDialog(price = price, currency = currency, onDismissRequest = {
            isPriceDialogVisible = false
            price = 0.0.formatAmount()
            mealBookId = null
        }, confirmButton = {
            onEvent.invoke(
                MealScreenEvents.AddMealBookEntry(
                    mealBookEntry = it.copy(
                        mealBookId = mealBookId ?: return@EditMealBookDialog,
                    ), onComplete = {
                        showToast(
                            "Meal Book Entry created successfully"
                        )
                        isPriceDialogVisible = false
                    })
            )
        })
    }
    MainContainer(
        modifier = modifier
            .nestedScroll(
                topAppBarScrollBehavior.nestedScrollConnection
            ),
        title = "Meal",
        scrollBehavior = topAppBarScrollBehavior,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Meal") },
                expanded = listState.handelFabState(),
                icon = {
                    Icon(Icons.TwoTone.Book, contentDescription = "Add Meal")
                }, onClick = onAddMealBookClick
            )
        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(MaterialTheme.spacing.medium),
            contentPadding = paddingValues,
            state = listState,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            items(state) {
                MealItem(
                    onMealItemClick = {
                        onItemClick.invoke(it)
                    },
                    totalPrice = calculateTotalPrice.invoke(it.mealBookId),
                    lastMonthPrice = calculateTotalLastMonthPrice.invoke(it.mealBookId),
                    state = it,
                    onActionClick = {
                        mealBookId = it.mealBookId
                        isPriceDialogVisible = true
                        currency = it.defaultCurrency
                        price = it.defaultPrice.formatAmount()
                    },
                    onlLongClick = {
                        yetEditModel = AddMealBookState(
                            mealBookId = it.mealBookId,
                            name = it.name,
                            defaultPrice = it.defaultPrice,
                            defaultCurrency = it.defaultCurrency,
                            createdAt = it.created,
                            description = it.description
                        )
                        isEditDialogVisible = true
                    }
                )
            }
            item {
                BottomPadding(
                    padding = MaterialTheme.spacing.bottomPadding * 2
                )
            }
        }
    }
}

@Preview
@Composable
private fun MealScreenPreview() {
    ExpenseSyncTheme {
        MealListScreen(
            state = emptyList(), calculateTotalPrice = { 0.0 },
            listState = rememberLazyListState()
        )
    }
}