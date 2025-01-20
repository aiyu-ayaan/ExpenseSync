package com.atech.expensesync.utils

enum class LoggerType {
    INFO,
    DEBUG,
    ERROR
}

expect fun expanseSyncLogger(
    message: String,
    loggerType: LoggerType = LoggerType.DEBUG
)