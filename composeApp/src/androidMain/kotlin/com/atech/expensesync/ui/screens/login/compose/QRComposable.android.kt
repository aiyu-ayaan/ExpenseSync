package com.atech.expensesync.ui.screens.login.compose

import androidx.compose.runtime.Composable

@Composable
actual fun QRComposable(): QRHelper = object : QRHelper {
    @Composable
    override fun generateContent(): @Composable (() -> Unit) = {}
}