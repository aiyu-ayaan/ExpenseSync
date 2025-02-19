package com.atech.expensesync.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable


@Composable
fun ExpenseSyncTheme(
    content: @Composable () -> Unit
) {
    val theme = getColorScheme(true)
    val colorScheme = /*if (isSystemInDarkTheme())
        theme.darkColorScheme
    else*/
        theme.lightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}