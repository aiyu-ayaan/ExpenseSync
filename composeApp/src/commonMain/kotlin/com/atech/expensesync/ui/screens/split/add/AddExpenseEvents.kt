package com.atech.expensesync.ui.screens.split.add

import com.atech.expensesync.database.room.split.ExpenseTransactions
import com.atech.expensesync.navigation.ViewExpanseBookArgs

sealed interface AddExpenseEvents {
    data class SetViewExpenseBookArgs(val args: ViewExpanseBookArgs?) : AddExpenseEvents
    data class SetTransactionForEdit(val model : ExpenseTransactions) : AddExpenseEvents
    object GetExpenseGroupMembers : AddExpenseEvents
    data class OnCreateExpenseStateChange(val state: CreateExpenseState) : AddExpenseEvents
    data object OnCreateExpenseStateReset : AddExpenseEvents
    data class AddExpenseToGroup(val onComplete: () -> Unit) : AddExpenseEvents
    data object LoadSettleUpScreen : AddExpenseEvents
}