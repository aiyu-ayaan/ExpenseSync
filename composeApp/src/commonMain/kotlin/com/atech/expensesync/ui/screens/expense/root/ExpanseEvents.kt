package com.atech.expensesync.ui.screens.expense.root

import com.atech.expensesync.database.room.expense.ExpenseBook

sealed interface ExpanseEvents {
    data class OnExpenseBookChange(
        val expenseBook: ExpenseBook
    ) : ExpanseEvents

    data object ResetStates : ExpanseEvents

    data class OnSaveExpense(
        val onComplete: (Long) -> Unit
    ) : ExpanseEvents

    data class OnExpenseBookClick(
        val expenseBook: ExpenseBook?
    ) : ExpanseEvents
}