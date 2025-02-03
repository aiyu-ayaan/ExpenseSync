package com.atech.expensesync.ui.screens.login.compose

import androidx.compose.runtime.Composable

interface QRHelper {
    @Composable
    fun generateContent(deviceUid: String): @Composable () -> Unit
}

@Composable
expect fun QRComposable(): QRHelper