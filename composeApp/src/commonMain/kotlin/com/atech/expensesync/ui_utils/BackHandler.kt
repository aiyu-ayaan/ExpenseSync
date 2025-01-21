package com.atech.expensesync.ui_utils

import androidx.compose.runtime.Composable

@Composable
expect fun BackHandler(enabled: Boolean = true, customAction: () -> Unit)