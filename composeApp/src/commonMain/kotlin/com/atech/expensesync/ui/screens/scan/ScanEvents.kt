package com.atech.expensesync.ui.screens.scan

import com.atech.expensesync.utils.ResponseDataState

sealed interface ScanEvents {
    data class OnScanSuccess(
        val key: String,
        val onDone: (ResponseDataState<String>) -> Unit
    ) : ScanEvents
}