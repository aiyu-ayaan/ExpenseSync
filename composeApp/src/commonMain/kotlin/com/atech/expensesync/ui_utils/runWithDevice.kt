package com.atech.expensesync.ui_utils

import androidx.compose.runtime.Composable

@Composable
inline fun runWithDeviceCompose(
    crossinline onAndroid: @Composable () -> Unit = {},
    crossinline onDesktop: @Composable () -> Unit = {}
) {
    if (com.atech.expensesync.utils.isAndroid()) {
        onAndroid()
    } else {
        onDesktop()
    }
}