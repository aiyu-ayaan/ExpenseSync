package com.atech.expensesync.ui.screens.meal.root.compose.add

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Description
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.atech.expensesync.LocalUploadDataHelper
import com.atech.expensesync.component.AppButton
import com.atech.expensesync.component.ChooseCurrencyBottomSheet
import com.atech.expensesync.component.ChooseIconBottomSheet
import com.atech.expensesync.component.EditTextEnhance
import com.atech.expensesync.component.EditTextPrice
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.component.mealIcons
import com.atech.expensesync.ui.screens.meal.root.AddMealBookState
import com.atech.expensesync.ui.screens.meal.root.MealScreenEvents
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.formatAmount
import com.atech.expensesync.ui_utils.showToast
import com.atech.expensesync.utils.LoggerType
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
    var showIconBottomSheet by remember { mutableStateOf(false) }
    var isDeleteDialogVisible by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val uploadDataHelper = LocalUploadDataHelper.current
    MainContainer(
        modifier = modifier,
        title = "Add Meal Book",
        onNavigationClick = onNavigationClick,
        actions = {
            AppButton(
                text = if (state.isEdit) "Update" else "Add",
                enable = state.defaultPrice != 0.0 && state.name.isNotEmpty(),
                onClick = {
                    if (state.isEdit.not()) onEvent.invoke(MealScreenEvents.OnAddMeal {
                        if (it > 0) {
                            showToast("Meal Book created successfully")
                            uploadDataHelper.uploadMealData(
                                onError = {
                                    com.atech.expensesync.utils.expenseSyncLogger(
                                        it, LoggerType.ERROR
                                    )
                                }) {
                                onNavigationClick()
                            }
                        } else {
                            showToast("Failed to create Meal Book.Check Meal Book name and try again")
                        }
                    })
                    else onEvent.invoke(MealScreenEvents.UpdateMealBook(state) { res ->
                        if (res < 0) {
                            showToast("Failed to create Meal Book.Check Meal Book name and try again")
                        } else {
                            showToast("Meal Book updated successfully")
                            uploadDataHelper.uploadMealData(
                                onError = {
                                    com.atech.expensesync.utils.expenseSyncLogger(
                                        it, LoggerType.ERROR
                                    )
                                }) {
                                onNavigationClick()
                            }
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

        AnimatedVisibility(showIconBottomSheet) {
            ChooseIconBottomSheet(
                modifier = Modifier, sheetState = sheetState, isForFood = true, onDismissRequest = {
                    scope.launch {
                        sheetState.hide()
                        showIconBottomSheet = false
                    }
                }) {
                onEvent.invoke(
                    MealScreenEvents.OnMealScreenStateChange(
                        state.copy(icon = it.displayName)
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
                    val icon =
                        (mealIcons.find { it.displayName == state.icon } ?: mealIcons.first()).icon
                    IconButton(onClick = {
                        showIconBottomSheet = !showIconBottomSheet
                    }) {
                        Icon(imageVector = icon, contentDescription = null)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Text,
                    imeAction = androidx.compose.ui.text.input.ImeAction.Next,
                    capitalization = androidx.compose.ui.text.input.KeyboardCapitalization.Sentences
                ),
                clearIconClick = {
                    onEvent.invoke(
                        MealScreenEvents.OnMealScreenStateChange(state.copy(name = ""))
                    )
                })
            EditTextPrice(
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Per meal cost",
                value = state.defaultPrice.toString(),
                onValueChange = { newValue ->
                    onEvent.invoke(
                        MealScreenEvents.OnMealScreenStateChange(
                            state.copy(
                                defaultPrice = newValue.toDoubleOrNull() ?: 0.0
                            )
                        )
                    )
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
                clearIconClick = {
                    onEvent.invoke(
                        MealScreenEvents.OnMealScreenStateChange(state.copy(defaultPrice = 0.0))
                    )
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
                ),
                clearIconClick = {
                    onEvent.invoke(
                        MealScreenEvents.OnMealScreenStateChange(state.copy(description = ""))
                    )
                })
            if (state.isEdit) TextButton(
                modifier = Modifier.fillMaxWidth(), onClick = {
                    isDeleteDialogVisible = true
                }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.Delete, contentDescription = null
                    )
                    Text(
                        "Delete"
                    )
                }
            }
        }

        if (isDeleteDialogVisible) {
            AlertDialog(icon = {
                Icon(
                    imageVector = Icons.TwoTone.Delete, contentDescription = "Delete Meal"
                )
            }, onDismissRequest = { isDeleteDialogVisible = false }, title = {
                Text("Delete Meal Book")
            }, text = {
                Text("Are you sure you want to delete this meal book?")
            }, confirmButton = {
                TextButton(onClick = {
                    onEvent.invoke(
                        MealScreenEvents.DeleteMealBook(
                            mealBookId = state.mealBookId, onComplete = {
                                uploadDataHelper.uploadMealData {
//                                    showToast("Meal Book deleted successfully")
                                    isDeleteDialogVisible = false
                                    onNavigationClick()
                                }
                            })
                    )

                }) {
                    Text("Delete")
                }
            }, dismissButton = {
                TextButton(onClick = { isDeleteDialogVisible = false }) {
                    Text("Cancel")
                }
            })
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