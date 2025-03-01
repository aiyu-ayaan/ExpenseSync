package com.atech.expensesync.ui.screens.meal.root.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Book
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.atech.expensesync.component.EditTextEnhance
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
import com.atech.expensesync.ui_utils.formatAmount
import com.atech.expensesync.ui_utils.isValidDecimalInput
import com.atech.expensesync.ui_utils.koinViewModel
import com.atech.expensesync.ui_utils.showToast
import com.atech.expensesync.utils.Currency
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
                MealListScreen(onAddMealBookClick = {
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
    var isPriceDialogVisible by remember { mutableStateOf(false) }
    var price by remember { mutableStateOf(0.0.formatAmount()) }
    var currency: String? by remember { mutableStateOf(null) }
    var mealBookId: String? by remember { mutableStateOf(null) }
    AnimatedVisibility(isPriceDialogVisible) {
        AlertDialog(icon = {
            Icon(
                imageVector = Icons.TwoTone.Book, contentDescription = "Add Meal"
            )
        }, onDismissRequest = {
            isPriceDialogVisible = false
            currency = null
            price = 0.0.formatAmount()
            mealBookId = null
        }, confirmButton = {
            TextButton(
                enabled = price != 0.0.formatAmount(),
                onClick = {
                    onEvent.invoke(
                        MealScreenEvents.AddMealBookEntry(
                            mealBookEntry = MealBookEntry(
                                mealBookId = mealBookId ?: return@TextButton,
                                price = price.toDoubleOrNull() ?: 0.0,
                            ), onComplete = {
                                showToast(
                                    "Meal Book Entry created successfully"
                                )
                                isPriceDialogVisible = false
                            })
                    )
                }
            ) {
                Text("Add")
            }
        }, dismissButton = {
            TextButton(onClick = {
                isPriceDialogVisible = false
            }) {
                Text("Cancel")
            }
        }, title = {
            Text("Add Meal Price")
        }, text = {
            EditTextEnhance(
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Per meal cost",
                value = price,
                clearIconClick = {
                    price = 0.0.formatAmount()
                },
                onValueChange = { newValue ->
                    if (newValue.isValidDecimalInput()) {
                        price = newValue
                    }
                },
                leadingIcon = {
                    IconButton(onClick = {}) {
                        Text(
                            text = currency ?: Currency.INR.symbol,
                            textAlign = TextAlign.Center,
                            fontSize = 24.sp,
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done
                )
            )
        })
    }
    MainContainer(
        modifier = modifier, title = "Meal", floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Meal") }, icon = {
                    Icon(Icons.TwoTone.Book, contentDescription = "Add Meal")
                }, onClick = onAddMealBookClick
            )
        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(MaterialTheme.spacing.medium),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            items(state) {
                MealItem(
                    totalPrice = calculateTotalPrice.invoke(it.mealBookId),
                    lastMonthPrice = calculateTotalLastMonthPrice.invoke(it.mealBookId),
                    state = it,
                    onActionClick = {
                        mealBookId = it.mealBookId
                        isPriceDialogVisible = true
                        currency = it.defaultCurrency.symbol
                        price = it.defaultPrice.formatAmount()
                    })
            }
        }
    }
}

@Preview
@Composable
private fun MealScreenPreview() {
    ExpenseSyncTheme {
        MealListScreen(
            state = emptyList(), calculateTotalPrice = { 0.0 })
    }
}