package com.atech.expensesync.ui.screens.expense.root.compose.add_edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import com.atech.expensesync.LocalUploadDataHelper
import com.atech.expensesync.component.AppButton
import com.atech.expensesync.component.ChooseCurrencyBottomSheet
import com.atech.expensesync.component.ChooseIconBottomSheet
import com.atech.expensesync.component.EditTextEnhance
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.component.expenseIcons
import com.atech.expensesync.database.room.expense.ExpenseBook
import com.atech.expensesync.ui.screens.expense.root.ExpenseEvents
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.atech.expensesync.ui.theme.spacing
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    modifier: Modifier = Modifier,
    state: ExpenseBook = ExpenseBook(""),
    onEvent: (ExpenseEvents) -> Unit = {},
    isEdit: Boolean = false,
    onNavigateClick: () -> Unit = {}
) {
    var showCurrencyBottomSheet by remember { mutableStateOf(false) }
    var showIconBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val uploadDataHelper = LocalUploadDataHelper.current
    MainContainer(
        modifier = modifier, title = if (isEdit) "Edit Expense"
        else "Add Expense", onNavigationClick = onNavigateClick
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(MaterialTheme.spacing.medium)
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            EditTextEnhance(
                modifier = Modifier.fillMaxWidth(),
                value = state.bookName,
                onValueChange = {
                    onEvent(ExpenseEvents.OnExpenseBookChange(state.copy(bookName = it)))
                },
                clearIconClick = {
                    onEvent(ExpenseEvents.OnExpenseBookChange(state.copy(bookName = "")))
                },
                placeholder = "Enter Book Name",
                keyboardOptions = KeyboardOptions(
                    imeAction = androidx.compose.ui.text.input.ImeAction.Done,
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Text,
                    capitalization = androidx.compose.ui.text.input.KeyboardCapitalization.Words
                ),
                leadingIcon = {
                    IconButton(onClick = {
                        showIconBottomSheet = !showIconBottomSheet
                    }) {
                        Icon(
                            imageVector = expenseIcons.find { it.displayName == state.icon }?.icon
                                ?: expenseIcons.first().icon,
                            contentDescription = "Icon"
                        )
                    }
                },
                trailingIcon = {
                    IconButton(onClick = {
                        showCurrencyBottomSheet = !showCurrencyBottomSheet
                    }) {
                        Text(
                            text = state.defaultCurrency.symbol,
                            textAlign = TextAlign.Center,
                            fontSize = 24.sp,
                        )
                    }
                }
            )
            Spacer(
                modifier = Modifier.padding(MaterialTheme.spacing.medium)
            )
            AppButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Save",
                enable = state.bookName.isNotEmpty(),
                onClick = {
                    onEvent(ExpenseEvents.OnSaveExpense {
                        if (it > 0) {
                            uploadDataHelper.uploadExpenseData {
                                onNavigateClick()
                            }
                            return@OnSaveExpense
                        }
                        com.atech.expensesync.ui_utils.showToast(
                            "Failed to save expense", com.atech.expensesync.ui_utils.Duration.LONG
                        )
                    })
                })
        }
    }
    AnimatedVisibility(showCurrencyBottomSheet) {
        ChooseCurrencyBottomSheet(
            modifier = Modifier, sheetState = sheetState, onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                    showCurrencyBottomSheet = false
                }
            }) {
            onEvent.invoke(
                ExpenseEvents.OnExpenseBookChange(
                    state.copy(defaultCurrency = it)
                )
            )
        }
    }

    AnimatedVisibility(showIconBottomSheet) {
        ChooseIconBottomSheet(
            modifier = Modifier, sheetState = sheetState, onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                    showIconBottomSheet = false
                }
            }) {
            onEvent.invoke(
                ExpenseEvents.OnExpenseBookChange(
                    state.copy(icon = it.displayName)
                )
            )
        }
    }
}

@Preview
@Composable
private fun AddEditScreenPreview() {
    ExpenseSyncTheme {
        AddEditScreen(
            state = ExpenseBook("Book Name")
        )
    }
}