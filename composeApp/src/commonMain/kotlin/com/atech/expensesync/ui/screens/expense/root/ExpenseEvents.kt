package com.atech.expensesync.ui.screens.expense.root

import com.atech.expensesync.database.room.expense.ExpenseBook
import com.atech.expensesync.database.room.expense.ExpenseBookEntry

sealed interface ExpenseEvents {
    data class OnExpenseBookChange(
        val expenseBook: ExpenseBook
    ) : ExpenseEvents

    data object ResetStates : ExpenseEvents

    data class OnSaveExpense(
        val onComplete: (Long) -> Unit
    ) : ExpenseEvents

    data class OnExpenseBookClick(
        val expenseBook: ExpenseBook?
    ) : ExpenseEvents

    data object LoadExpenseBookEntry : ExpenseEvents

    data class OnSaveExpenseBookEntry(
        val expenseBookEntry: ExpenseBookEntry,
        val onComplete: () -> Unit
    ) : ExpenseEvents
}