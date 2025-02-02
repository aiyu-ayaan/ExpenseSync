package com.atech.expensesync.utils

import android.util.Log

actual fun expenseSyncLogger(
    message: String, loggerType: LoggerType
) {
    Log.d("AAA","Called")
    when (loggerType) {
        LoggerType.DEBUG -> Log.d("ExpanseSyncLogger", message)
        LoggerType.ERROR -> Log.e("ExpanseSyncLogger", message)
        LoggerType.INFO -> Log.i("ExpanseSyncLogger", message)
    }
}