package com.atech.expensesync.ui_utils

import android.widget.Toast
import com.atech.expensesync.ExpenseSync
import com.atech.expensesync.utils.LoggerType
import com.atech.expensesync.utils.expenseSyncLogger

actual fun showToast(message: String, duration: Duration) {
    ExpenseSync.instance?.let {
        Toast.makeText(
            it,
            message,
            if (duration == Duration.SHORT) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
        ).show()
    } ?: {
        expenseSyncLogger("ExpenseSync instance is null",LoggerType.ERROR)
    }
}