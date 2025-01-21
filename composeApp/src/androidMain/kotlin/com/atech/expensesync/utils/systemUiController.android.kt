package com.atech.expensesync.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
actual fun systemUiController(
    bottomNavigationBarColor: Color,
    statusBarColor: Color,
    isDarkIcon: Boolean
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = bottomNavigationBarColor,
            darkIcons = !isDarkIcon
        )
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = !isDarkIcon
        )
    }
}