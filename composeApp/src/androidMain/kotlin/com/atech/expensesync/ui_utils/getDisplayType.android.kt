package com.atech.expensesync.ui_utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.window.core.layout.WindowWidthSizeClass

@Composable
actual fun getDisplayType(): DeviceType {
    val configuration = LocalConfiguration.current
    val windowWidthSizeClass = when {
        configuration.screenWidthDp < 600 -> WindowWidthSizeClass.COMPACT
        configuration.screenWidthDp < 840 -> WindowWidthSizeClass.MEDIUM
        else -> WindowWidthSizeClass.EXPANDED
    }
    return when (windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT -> DeviceType.MOBILE
        WindowWidthSizeClass.MEDIUM -> DeviceType.TABLET
        else -> DeviceType.DESKTOP
    }
}