package com.atech.expensesync.utils

actual fun expanseSyncLogger(
    message: String,
    loggerType: LoggerType
) {
    when (loggerType) {
        LoggerType.DEBUG -> println("DEBUG: $message")
        LoggerType.ERROR -> System.err.print("ERROR: $message")
        LoggerType.INFO -> println("INFO: $message")
    }
}