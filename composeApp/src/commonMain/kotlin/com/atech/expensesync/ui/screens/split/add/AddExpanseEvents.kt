package com.atech.expensesync.ui.screens.split.add

import com.atech.expensesync.navigation.ViewExpanseBookArgs

sealed interface AddExpanseEvents {
    data class SetViewExpanseBookArgs(val args: ViewExpanseBookArgs?) : AddExpanseEvents
    object GetExpanseGroupMembers : AddExpanseEvents
}