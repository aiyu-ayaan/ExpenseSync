package com.atech.expensesync.utils

import android.util.Log

actual fun expanseSyncLogger(
    message: String, loggerType: LoggerType
) {
    Log.d("AAA","Called")
    when (loggerType) {
        LoggerType.DEBUG -> android.util.Log.d("ExpanseSyncLogger", message)
        LoggerType.ERROR -> android.util.Log.e("ExpanseSyncLogger", message)
        LoggerType.INFO -> android.util.Log.i("ExpanseSyncLogger", message)
    }
}