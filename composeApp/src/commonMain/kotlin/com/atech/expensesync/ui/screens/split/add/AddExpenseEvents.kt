package com.atech.expensesync.ui.screens.split.add

import com.atech.expensesync.database.room.split.SplitTransactions
import com.atech.expensesync.navigation.ViewExpanseBookArgs

sealed interface AddExpenseEvents {
    data class SetViewExpenseBookArgs(val args: ViewExpanseBookArgs?) : AddExpenseEvents
    data class SetTransactionForEdit(val model : SplitTransactions) : AddExpenseEvents
    data object GetExpenseGroupMembers : AddExpenseEvents
    data class OnCreateExpenseStateChange(val state: CreateExpenseState) : AddExpenseEvents
    data object OnCreateExpenseStateReset : AddExpenseEvents
    data class AddExpenseToGroup(val onComplete: () -> Unit) : AddExpenseEvents
    data object LoadSettleUpScreen : AddExpenseEvents
    data object InsertNewGroupMember : AddExpenseEvents
}