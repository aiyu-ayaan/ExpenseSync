package com.atech.expensesync.ui.screens.meal.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Book
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Description
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.atech.expensesync.component.EditTextEnhance
import com.atech.expensesync.ui.screens.meal.root.AddMealBookState
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.formatAmount
import com.atech.expensesync.ui_utils.isValidDecimalInput

@Composable
fun EditMealDialog(
    modifier: Modifier = Modifier,
    mealBook: AddMealBookState,
    canShowDeleteOption: Boolean = true,
    onDismissRequest: () -> Unit,
    onDeleteItem: () -> Unit = {},
    confirmButton: (AddMealBookState) -> Unit,
) {
    var state by remember(mealBook) {
        mutableStateOf(mealBook)
    }
    var price by remember { mutableStateOf(state.defaultPrice.formatAmount()) }
    var isDeleteDialogVisible by remember { mutableStateOf(false) }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        icon = {
            Icon(
                imageVector = Icons.TwoTone.Edit,
                contentDescription = "Edit Meal Book"
            )
        },
        title = {
            Text(text = "Edit Meal Book")
        },
        text = {
            Column(
                modifier = Modifier.padding(MaterialTheme.spacing.medium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
            ) {
                EditTextEnhance(
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = "Meal Book Name",
                    value = state.name,
                    onValueChange = {
                        state = state.copy(name = it)
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
                    ),
                    clearIconClick = {
                        state = state.copy(name = "")
                    }
                )
                EditTextEnhance(
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = "Per meal cost",
                    value = price,
                    onValueChange = { newValue ->
                        if (newValue.isValidDecimalInput()) {
                            price = newValue
                            state =
                                state.copy(
                                    defaultPrice = newValue.toDoubleOrNull() ?: 0.0
                                )
                        }
                    },
                    leadingIcon = {
                        IconButton(onClick = {}) {
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
                    ),
                    clearIconClick = {
                        price = ""
                        state = state.copy(defaultPrice = 0.0)
                    }
                )
                EditTextEnhance(
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = "Description",
                    value = state.description,
                    onValueChange = {
                        state = state.copy(description = it)
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
                        state = state.copy(description = "")
                    }
                )
                if (canShowDeleteOption)
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
        confirmButton = {
            TextButton(
                enabled = state.defaultPrice != 0.0 && state.name.isNotEmpty(),
                onClick = {
                    confirmButton(
                        state
                    )
                }) {
                Text("Update")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
    if (isDeleteDialogVisible) {
        AlertDialog(
            icon = {
                Icon(
                    imageVector = Icons.TwoTone.Delete, contentDescription = "Delete Meal"
                )
            },
            onDismissRequest = { isDeleteDialogVisible = false },
            title = {
                Text("Delete Meal Book")
            },
            text = {
                Text("Are you sure you want to delete this meal book?")
            },
            confirmButton = {
                TextButton(onClick = onDeleteItem) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { isDeleteDialogVisible = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}