package com.atech.expensesync.ui_utils

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(enabled: Boolean, customAction: () -> Unit) {
    // no need to handle back press on desktop
}