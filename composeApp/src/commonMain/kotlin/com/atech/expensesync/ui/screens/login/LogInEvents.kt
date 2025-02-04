package com.atech.expensesync.ui.screens.login

import com.atech.expensesync.database.models.User
import com.atech.expensesync.utils.ResponseDataState

sealed interface LogInEvents {
    data class OnLogInClicked(
        val model: User,
        val onSuccess: (ResponseDataState<User>) -> Unit,
    ) : LogInEvents

    data class StartWebSocket(
        val desktopUid: String,
    ) : LogInEvents

    data object StopWebSocket : LogInEvents
}