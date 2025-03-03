package com.atech.expensesync.ui.screens.meal.edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Book
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Today
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.atech.expensesync.component.EditTextEnhance
import com.atech.expensesync.component.TextItem
import com.atech.expensesync.database.room.meal.MealBookEntry
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.formatAmount
import com.atech.expensesync.ui_utils.isValidDecimalInput
import com.atech.expensesync.utils.Currency
import com.atech.expensesync.utils.DatePattern
import com.atech.expensesync.utils.convertToDateFormat
import kotlinx.datetime.TimeZone
import java.util.Calendar

@Composable
fun EditMealBookDialog(
    modifier: Modifier = Modifier,
    isEdit: Boolean = false,
    price: String,
    description: String = "",
    date: Long = System.currentTimeMillis(),
    currency: Currency = Currency.INR,
    onDismissRequest: () -> Unit,
    onDeleteItem: () -> Unit = {},
    confirmButton: (MealBookEntry) -> Unit,
) {
    var editablePrice by remember {
        mutableStateOf(price)
    }
    var editableDescription by remember {
        mutableStateOf(description)
    }
    var editableDate by remember {
        mutableStateOf(date)
    }
    var isDatePickerVisible by remember {
        mutableStateOf(false)
    }

    var isDeleteDialogVisible by remember {
        mutableStateOf(false)
    }

    AnimatedVisibility(isDeleteDialogVisible) {
        AlertDialog(
            icon = {
                Icon(
                    imageVector = Icons.TwoTone.Delete, contentDescription = "Delete Meal"
                )
            },
            title = {
                Text("Delete Meal Book")
            },
            text = {
                Text("Are you sure you want to delete this meal book?")
            },
            onDismissRequest = { isDeleteDialogVisible = false },
            confirmButton = {
                TextButton(onClick = {
                    isDeleteDialogVisible = false
                    onDeleteItem()
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    isDeleteDialogVisible = false
                }) {
                    Text("Cancel")
                }
            }
        )
    }
    AnimatedVisibility(isDatePickerVisible) {
        DatePickerModal(onDateSelected = {
            editableDate = it ?: editableDate
            isDatePickerVisible = false
        }, onDismiss = {
            isDatePickerVisible = false
        })
    }
    AlertDialog(
        modifier = modifier,
        icon = {
            Icon(
                imageVector = Icons.TwoTone.Book, contentDescription = "Add Meal"
            )
        },
        title = {
            Text("Add Meal Price")
        },
        text = {
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
            ) {
                TextItem(
                    containerColor = AlertDialogDefaults.containerColor,
                    modifier = Modifier,
                    text = "Date : ${editableDate.convertToDateFormat(DatePattern.DD_MM_YYYY_HH_MM_A)}",
                    endIcon = Icons.TwoTone.Today,
                    onClick = {
                        isDatePickerVisible = !isDatePickerVisible
                    },
                    onEndIconClick = {
                        isDatePickerVisible = !isDatePickerVisible
                    }
                )
                EditTextEnhance(
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = "Per meal cost",
                    value = editablePrice,
                    clearIconClick = {
                        editablePrice = 0.0.formatAmount()
                    },
                    supportingText = {
                        Text("Set default if you don't want to change")
                    },
                    onValueChange = { newValue ->
                        if (newValue.isValidDecimalInput()) {
                            editablePrice = newValue
                        }
                    },
                    leadingIcon = {
                        IconButton(onClick = {}) {
                            Text(
                                text = currency.symbol,
                                textAlign = TextAlign.Center,
                                fontSize = 24.sp,
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next
                    )
                )
                EditTextEnhance(
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = "Description",
                    value = editableDescription,
                    clearIconClick = {
                        editableDescription = ""
                    },
                    onValueChange = { newValue ->
                        editableDescription = newValue
                    },
                    supportingText = {
                        Text("Add description\nLeave empty if not required")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.Sentences
                    )
                )
                if (isEdit)
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            isDeleteDialogVisible = true
                        }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
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
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                enabled = editablePrice != 0.0.formatAmount(), onClick = {
                    confirmButton(
                        MealBookEntry(
                            price = editablePrice.toDoubleOrNull() ?: 0.0,
                            description = editableDescription,
                            mealBookId = "",
                            createdAt = editableDate
                        )
                    )
                }) {
                Text("Update")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit, onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(onDismissRequest = onDismiss, confirmButton = {
        TextButton(onClick = {
            val selectedDateMillis = datePickerState.selectedDateMillis
            val dateWithCurrentTime = selectedDateMillis?.let { getDateWithCurrentTime(it) }

            onDateSelected(dateWithCurrentTime)
            onDismiss()
        }) {
            Text("OK")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    }) {
        DatePicker(state = datePickerState)
    }
}

private fun getDateWithCurrentTime(dateMillis: Long): Long {
    val timeZone = TimeZone.currentSystemDefault().id
    val selectedDate = Calendar.getInstance(java.util.TimeZone.getTimeZone(timeZone)).apply {
        timeInMillis = dateMillis
    }

    val currentTime = Calendar.getInstance(java.util.TimeZone.getTimeZone(timeZone))

    selectedDate.set(Calendar.HOUR_OF_DAY, currentTime.get(Calendar.HOUR_OF_DAY))
    selectedDate.set(Calendar.MINUTE, currentTime.get(Calendar.MINUTE))
    selectedDate.set(Calendar.SECOND, currentTime.get(Calendar.SECOND))
    selectedDate.set(Calendar.MILLISECOND, currentTime.get(Calendar.MILLISECOND))

    return selectedDate.timeInMillis
}