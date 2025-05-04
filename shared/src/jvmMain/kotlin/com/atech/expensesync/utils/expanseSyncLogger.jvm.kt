package com.atech.expensesync.utils

actual fun expenseSyncLogger(
    message: String,
    loggerType: LoggerType
) {
    when (loggerType) {
        LoggerType.DEBUG -> println("Aiyu: DEBUG $message")
        LoggerType.ERROR -> System.err.print("Aiyu: ERROR $message")
        LoggerType.INFO -> println("Aiyu: INFO $message")
    }
}