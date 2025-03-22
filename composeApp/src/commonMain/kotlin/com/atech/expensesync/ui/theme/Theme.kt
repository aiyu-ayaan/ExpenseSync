package com.atech.expensesync.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


val ColorScheme.captionColor: Color
    @Composable
    get() = onSurface.copy(alpha = 0.6f)

val ColorScheme.appGreen: Color
    @Composable
    get() = Color(0xFF127E40)

val ColorScheme.appRed: Color
    @Composable
    get() = Color(0xFFC73B3A)

@Composable
fun ExpenseSyncTheme(
    content: @Composable () -> Unit
) {
    val theme = getColorScheme(true)
    val colorScheme = if (isSystemInDarkTheme())
        theme.darkColorScheme
    else
        theme.lightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}