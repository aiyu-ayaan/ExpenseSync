package com.atech.expensesync.ui.theme

import androidx.compose.runtime.Composable

@Composable
internal actual fun getColorScheme(
    isEnableDynamicColor: Boolean
): ExpenseSyncColorScheme =
    defaultColorScheme