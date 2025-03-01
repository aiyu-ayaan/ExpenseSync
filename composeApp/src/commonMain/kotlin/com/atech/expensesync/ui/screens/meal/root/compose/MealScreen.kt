package com.atech.expensesync.ui.screens.meal.root.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Book
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
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.room.meal.MealBook
import com.atech.expensesync.database.room.meal.MealBookEntry
import com.atech.expensesync.ui.screens.meal.root.AddMealBookState
import com.atech.expensesync.ui.screens.meal.root.MealScreenEvents
import com.atech.expensesync.ui.screens.meal.root.MealViewModel
import com.atech.expensesync.ui.screens.meal.root.compose.add.AddMealBookScreen
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.backHandlerThreePane
import com.atech.expensesync.ui_utils.koinViewModel
import com.atech.expensesync.ui_utils.showToast
import com.atech.expensesync.utils.checkIts1stDayOfMonth
import org.jetbrains.compose.ui.tooling.preview.Preview

//private enum class DetailsScreenType {
//    AddMealBook, None
//}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MealScreen(
    modifier: Modifier = Modifier,
    canShowAppBar: (Boolean) -> Unit,
    navHostController: NavHostController
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    val viewModel = koinViewModel<MealViewModel>()
    val mealBookItems by viewModel.mealBooks.collectAsState(emptyList())
    navigator.backHandlerThreePane(backAction = {
//        detailsScreenType = DetailsScreenType.None
        viewModel.onEvent(MealScreenEvents.OnMealScreenStateChange(null))
    })
    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                canShowAppBar.invoke(true)
                MealListScreen(
                    onAddMealBookClick = {
                        viewModel.onEvent(MealScreenEvents.OnMealScreenStateChange(AddMealBookState()))
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Extra)
                    },
                    state = mealBookItems,
                    onEvent = viewModel::onEvent,
                    calculateTotalPrice = {
                        val value by viewModel.calculateTotalForCurrentMonth(it)
                            .collectAsState(0.0)
                        value
                    },
                    calculateTotalLastMonthPrice = {
                        if (checkIts1stDayOfMonth()) {
                            val value by viewModel.calculateTotalForLastMonth(it)
                                .collectAsState(0.0)
                            value
                        } else 0.0
                    }
                )
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
        detailPane = {}/* when (detailsScreenType) {
            DetailsScreenType.AddMealBook -> {
                {

                }
            }

            DetailsScreenType.None -> {
                {}
            }
        }*/
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MealListScreen(
    modifier: Modifier = Modifier,
    onAddMealBookClick: () -> Unit = {},
    state: List<MealBook>,
    calculateTotalPrice: @Composable (String) -> Double,
    calculateTotalLastMonthPrice: @Composable (String) -> Double = { 0.0 },
    onEvent: (MealScreenEvents) -> Unit = {}
) {
    MainContainer(
        modifier = modifier, title = "Meal", floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Meal") }, icon = {
                    Icon(Icons.TwoTone.Book, contentDescription = "Add Meal")
                }, onClick = onAddMealBookClick
            )
        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(MaterialTheme.spacing.medium),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            items(state) {
                MealItem(
                    totalPrice = calculateTotalPrice.invoke(it.mealBookId),
                    lastMonthPrice = calculateTotalLastMonthPrice.invoke(it.mealBookId),
                    state = it,
                    onActionClick = {
                        onEvent.invoke(
                            MealScreenEvents.AddMealBookEntry(
                                mealBookEntry = MealBookEntry(
                                    mealBookId = it.mealBookId,
                                    price = it.defaultPrice,
                                ),
                                onComplete = {
                                    showToast(
                                        "Meal Book Entry created successfully"
                                    )
                                }
                            )
                        )
                    }
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
            state = emptyList(),
            calculateTotalPrice = { 0.0 }
        )
    }
}