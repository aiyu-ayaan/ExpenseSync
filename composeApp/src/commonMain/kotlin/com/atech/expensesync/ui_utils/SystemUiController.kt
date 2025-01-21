package com.atech.expensesync.ui_utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
expect fun SystemUiController(
    bottomNavigationBarColor: Color = MaterialTheme.colorScheme.background,
    statusBarColor: Color = MaterialTheme.colorScheme.primary,
    isDarkIcon : Boolean = isSystemInDarkTheme()
)