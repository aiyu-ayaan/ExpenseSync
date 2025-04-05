package com.atech.expensesync.ui.screens.scan

import com.atech.expensesync.database.models.DesktopLogInDetails
import com.atech.expensesync.firebase.util.FirebaseResponse

sealed interface ScanEvents {
    data class OnScanSuccess(
        val key: String,
        val onDone: (FirebaseResponse<DesktopLogInDetails>) -> Unit
    ) : ScanEvents

    data object PerformLogOut : ScanEvents
}