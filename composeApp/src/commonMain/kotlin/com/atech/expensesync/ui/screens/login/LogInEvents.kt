package com.atech.expensesync.ui.screens.login

import com.atech.expensesync.database.models.User
import com.atech.expensesync.firebase.util.FirebaseResponse

sealed interface LogInEvents {
    data class OnLogInClicked(
        val model: User,
        val onSuccess: (FirebaseResponse<User>) -> Unit,
    ) : LogInEvents

    data class ObserveLogInData(
        val desktopUid: String,
    ) : LogInEvents


}