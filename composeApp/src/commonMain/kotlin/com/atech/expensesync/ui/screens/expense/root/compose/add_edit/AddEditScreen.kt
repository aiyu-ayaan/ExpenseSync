package com.atech.expensesync.ui.screens.expense.root.compose.add_edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.atech.expensesync.component.AppButton
import com.atech.expensesync.component.EditTextEnhance
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.room.expense.ExpenseBook
import com.atech.expensesync.ui.screens.expense.root.ExpanseEvents
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.atech.expensesync.ui.theme.spacing
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    modifier: Modifier = Modifier,
    state: ExpenseBook = ExpenseBook(""),
    onEvent: (ExpanseEvents) -> Unit = {},
    isEdit: Boolean = false,
    onNavigateClick: () -> Unit = {}
) {
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
                    onEvent(ExpanseEvents.OnExpenseBookChange(state.copy(bookName = it)))
                },
                clearIconClick = {
                    onEvent(ExpanseEvents.OnExpenseBookChange(state.copy(bookName = "")))
                },
                placeholder = "Enter Book Name",
                keyboardOptions = KeyboardOptions(
                    imeAction = androidx.compose.ui.text.input.ImeAction.Done,
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Text,
                    capitalization = androidx.compose.ui.text.input.KeyboardCapitalization.Words
                )
            )
            Spacer(
                modifier = Modifier.padding(MaterialTheme.spacing.medium)
            )
            AppButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Save",
                enable = state.bookName.isNotEmpty(),
                onClick = {
                    onEvent(ExpanseEvents.OnSaveExpense {
                        if (it > 0) {
                            onNavigateClick()
                            return@OnSaveExpense
                        }
                        com.atech.expensesync.ui_utils.showToast(
                            "Failed to save expense", com.atech.expensesync.ui_utils.Duration.LONG
                        )
                    })
                })
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