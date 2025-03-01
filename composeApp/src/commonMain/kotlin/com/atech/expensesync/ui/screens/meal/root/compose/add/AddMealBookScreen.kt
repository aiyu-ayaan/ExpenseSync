package com.atech.expensesync.ui.screens.meal.root.compose.add

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Book
import androidx.compose.material.icons.twotone.Description
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.atech.expensesync.component.AppButton
import com.atech.expensesync.component.ChooseCurrencyBottomSheet
import com.atech.expensesync.component.EditTextEnhance
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.ui.screens.meal.root.AddMealBookState
import com.atech.expensesync.ui.screens.meal.root.MealScreenEvents
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.formatAmount
import com.atech.expensesync.ui_utils.isValidDecimalInput
import com.atech.expensesync.ui_utils.showToast
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealBookScreen(
    modifier: Modifier = Modifier,
    state: AddMealBookState,
    onEvent: (MealScreenEvents) -> Unit = {},
    onNavigationClick: () -> Unit = {}
) {
    var price by remember { mutableStateOf(state.defaultPrice.formatAmount()) }
    var showCurrencyBottomSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    MainContainer(
        modifier = modifier,
        title = "Add Meal Book",
        onNavigationClick = onNavigationClick,
        actions = {
            AppButton(
                text = "Create",
                enable = state.defaultPrice != 0.0 && state.name.isNotEmpty(),
                onClick = {
                    onEvent.invoke(MealScreenEvents.OnAddMeal {
                        if (it > 0) {
                            showToast("Meal Book created successfully")
                            onNavigationClick()
                        } else {
                            showToast("Failed to create Meal Book.Check Meal Book name and try again")
                        }
                    })
                })
        }) { paddingValues ->
        AnimatedVisibility(showCurrencyBottomSheet) {
            ChooseCurrencyBottomSheet(
                modifier = Modifier, sheetState = sheetState, onDismissRequest = {
                    scope.launch {
                        sheetState.hide()
                        showCurrencyBottomSheet = false
                    }
                }) {
                onEvent.invoke(
                    MealScreenEvents.OnMealScreenStateChange(
                        state.copy(defaultCurrency = it)
                    )
                )
            }
        }

        Column(
            modifier = Modifier.padding(paddingValues).padding(MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            EditTextEnhance(
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Meal Book Name",
                value = state.name,
                onValueChange = {
                    onEvent.invoke(
                        MealScreenEvents.OnMealScreenStateChange(state.copy(name = it))
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.TwoTone.Book, contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Text,
                    imeAction = androidx.compose.ui.text.input.ImeAction.Next,
                    capitalization = androidx.compose.ui.text.input.KeyboardCapitalization.Sentences
                )
            )
            EditTextEnhance(
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Per meal cost",
                value = price,
                onValueChange = { newValue ->
                    if (newValue.isValidDecimalInput()) {
                        price = newValue
                        onEvent.invoke(
                            MealScreenEvents.OnMealScreenStateChange(
                                state.copy(
                                    defaultPrice = newValue.toDoubleOrNull() ?: 0.0
                                )
                            )
                        )
                    }
                },
                leadingIcon = {
                    IconButton(onClick = {
                        showCurrencyBottomSheet = !showCurrencyBottomSheet
                    }) {
                        Text(
                            text = state.defaultCurrency.symbol,
                            textAlign = TextAlign.Center,
                            fontSize = 24.sp,
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal,
                    imeAction = androidx.compose.ui.text.input.ImeAction.Done
                )
            )
            EditTextEnhance(
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Meal Book Description",
                value = state.description,
                onValueChange = {
                    onEvent.invoke(
                        MealScreenEvents.OnMealScreenStateChange(state.copy(description = it))
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.TwoTone.Description, contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Text,
                    imeAction = androidx.compose.ui.text.input.ImeAction.Done,
                    capitalization = androidx.compose.ui.text.input.KeyboardCapitalization.Sentences
                )
            )
        }
    }
}

@Preview
@Composable
private fun AddMealBookScreenPreview() {
    ExpenseSyncTheme {
        AddMealBookScreen(
            state = AddMealBookState()
        )
    }
}