package com.atech.expensesync.utils

actual fun restartApp() {
    // Android does not support restarting the app programmatically.
    // You can use the following code to restart the app:
    // val intent = Intent(context, MainActivity::class.java)
    // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    // context.startActivity(intent)
    // exitProcess(0)
}