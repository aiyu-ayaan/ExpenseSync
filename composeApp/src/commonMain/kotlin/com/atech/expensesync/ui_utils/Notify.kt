package com.atech.expensesync.ui_utils

enum class Duration {
    SHORT,
    LONG
}

expect fun showToast(message: String, duration: Duration = Duration.SHORT)