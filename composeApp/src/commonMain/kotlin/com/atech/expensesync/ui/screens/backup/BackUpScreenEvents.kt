package com.atech.expensesync.ui.screens.backup

sealed interface BackUpScreenEvents {
    data object OnMealDataBackUpDone : BackUpScreenEvents
    data object OnExpenseDataBackUpDone : BackUpScreenEvents
    data object OnSplitDataBackUpDone : BackUpScreenEvents
    data object OnDataBackUpDone : BackUpScreenEvents
}