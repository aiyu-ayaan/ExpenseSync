package com.atech.expensesync.ui.screens.split.add

import com.atech.expensesync.navigation.ViewExpanseBookArgs

sealed interface AddExpenseEvents {
    data class SetViewExpenseBookArgs(val args: ViewExpanseBookArgs?) : AddExpenseEvents
    object GetExpenseGroupMembers : AddExpenseEvents
    data class OnExpanseGroupMembers(val state: CreateExpenseState) : AddExpenseEvents
}