package com.atech.expensesync.utils

expect fun isAndroid(): Boolean


inline fun runWithDevice(
    crossinline onAndroid: () -> Unit = {},
    crossinline onDesktop: () -> Unit = {}
) {
    if (isAndroid()) {
        onAndroid()
    } else {
        onDesktop()
    }
}