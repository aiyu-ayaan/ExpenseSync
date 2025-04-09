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
import com.atech.expensesync.LocalUploadDataHelper
import com.atech.expensesync.component.BottomPadding
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.room.meal.MealBook
import com.atech.expensesync.firebase.util.FirebaseResponse
import com.atech.expensesync.firebase.util.getError
import com.atech.expensesync.firebase.util.getOrNull
import com.atech.expensesync.firebase.util.isError
import com.atech.expensesync.firebase.util.isSuccess
import com.atech.expensesync.ui.screens.meal.edit.EditMealBookEntryDialog
import com.atech.expensesync.ui.screens.meal.root.AddMealBookState
import com.atech.expensesync.ui.screens.meal.root.MealScreenEvents
import com.atech.expensesync.ui.screens.meal.root.MealViewModel
import com.atech.expensesync.ui.screens.meal.root.compose.add.AddMealBookScreen
import com.atech.expensesync.ui.screens.meal.view.ViewMealEvents
import com.atech.expensesync.ui.screens.meal.view.ViewMealViewModel
import com.atech.expensesync.ui.screens.meal.view.compose.ViewMealScreen
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.backHandlerThreePane
import com.atech.expensesync.ui_utils.formatAmount
import com.atech.expensesync.ui_utils.showToast
import com.atech.expensesync.utils.Currency
import com.atech.expensesync.utils.checkIts1stDayOfMonth
import com.atech.expensesync.utils.expenseSyncLogger
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
    val mealBookItems by viewModel.mealBooks.collectAsState(FirebaseResponse.Loading)
    viewModel.backUpMealEntries()

    val lazyListState = rememberLazyListState()
    navigator.backHandlerThreePane()
    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                canShowAppBar.invoke(true)
                MealListScreen(listState = lazyListState, onItemClick = {
                    /*navHostController.navigate(*/
                    viewModel.onEvent(
                        MealScreenEvents.SetMealBookEntry(
                            AddMealBookState(
                                icon = it.icon,
                                mealBookId = it.mealBookId,
                                name = it.name,
                                defaultPrice = it.defaultPrice,
                                defaultCurrency = it.defaultCurrency,
                                createdAt = it.created,
                                description = it.description,
                            )
                        )
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
                        val value by viewModel.calculateTotalForLastMonth(it).collectAsState(0.0)
                        value
                    } else 0.0
                }, onLongClick = {
                    if (navigator.currentDestination?.pane == ListDetailPaneScaffoldRole.Detail) {
                        return@MealListScreen
                    }
                    viewModel.onEvent(
                        MealScreenEvents.SetMealBookEntry(
                            it
                        )
                    )
                    navigator.navigateTo(
                        ListDetailPaneScaffoldRole.Extra
                    )
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
                        navigator.navigateBack()
                    })
            }
        },
        detailPane = if (viewModel.addMealState.value != null) {
            {
                val arg = viewModel.addMealState.value
                AnimatedPane {
                    canShowAppBar.invoke(false)
                    mealViewModel.onEvent(
                        ViewMealEvents.SetMealBookId(
                            arg!!.mealBookId, arg
                        )
                    )
                    val calenderMonth by mealViewModel.calenderMonth
                    val mealBookEntryState by mealViewModel.mealBookEntryState
                    ViewMealScreen(
                        mealBookState = mealViewModel.mealBookState.value ?: arg,
                        state = mealBookEntryState,
                        calenderMonth = calenderMonth,
                        onEvent = mealViewModel::onEvent,
                        onNavigateUp = {
                            navigator.navigateBack()
                            viewModel.onEvent(MealScreenEvents.OnMealScreenStateChange(null))
                        },
                        onEditClick = {
                            viewModel.onEvent(
                                MealScreenEvents.SetMealBookEntry(
                                    arg.copy(
                                        isEdit = true
                                    )
                                )
                            )
                            navigator.navigateTo(
                                ListDetailPaneScaffoldRole.Extra
                            )
                        })
                }
            }
        } else {
            {}
        })
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
    state: FirebaseResponse<List<MealBook>>,
    listState: LazyListState,
    onLongClick: (AddMealBookState) -> Unit = {},
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
    var isLoaded by remember { mutableStateOf(false) }
    val uploadDataHelper = LocalUploadDataHelper.current


    AnimatedVisibility(isPriceDialogVisible) {
        EditMealBookEntryDialog(price = price, currency = currency, onDismissRequest = {
            isPriceDialogVisible = false
            price = 0.0.formatAmount()
            mealBookId = null
        }, confirmButton = {
            onEvent.invoke(
                MealScreenEvents.AddMealBookEntry(
                    mealBookEntry = it.copy(
                        mealBookId = mealBookId ?: return@EditMealBookEntryDialog,
                    ), onComplete = {
                        showToast(
                            "Meal Book Entry created successfully"
                        )
                        uploadDataHelper.uploadMealData {
                            isPriceDialogVisible = false

                        }
                    })
            )
        })
    }

    MainContainer(
        modifier = modifier.nestedScroll(
            topAppBarScrollBehavior.nestedScrollConnection
        ), title = "Meal", scrollBehavior = topAppBarScrollBehavior, floatingActionButton = {
            AnimatedVisibility(state.isSuccess()) {
                ExtendedFloatingActionButton(
                    text = { Text("Add Meal") },
                    expanded = listState.handelFabState(),
                    icon = {
                        Icon(Icons.TwoTone.Book, contentDescription = "Add Meal")
                    },
                    onClick = onAddMealBookClick
                )
            }
        }) { paddingValues ->
        val data = state.getOrNull() ?: emptyList()
        if (state.isError()) {
            expenseSyncLogger(
                "Error in meal book list: ${state.getError()}",
                com.atech.expensesync.utils.LoggerType.ERROR
            )
        }
//        TODO: Handle error state and also handle update and delete state
        isLoaded = true
        AnimatedVisibility(isLoaded) {
            LazyColumn(
                modifier = Modifier.padding(MaterialTheme.spacing.medium),
                contentPadding = paddingValues,
                state = listState,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
            ) {
                items(data) {
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
                            onLongClick.invoke(
                                AddMealBookState(
                                    icon = it.icon,
                                    mealBookId = it.mealBookId,
                                    name = it.name,
                                    defaultPrice = it.defaultPrice,
                                    defaultCurrency = it.defaultCurrency,
                                    createdAt = it.created,
                                    description = it.description,
                                    isEdit = true
                                )
                            )
                        })
                }
                item {
                    BottomPadding(
                        padding = MaterialTheme.spacing.bottomPadding * 2
                    )
                }
            }
        }
    }
}
