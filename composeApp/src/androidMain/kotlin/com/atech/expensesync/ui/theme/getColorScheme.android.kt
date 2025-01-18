package com.atech.expensesync.ui.theme

import android.os.Build
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
internal actual fun getColorScheme(
    isEnableDynamicColor: Boolean
): ExpenseSyncColorScheme = if (!isEnableDynamicColor) defaultColorScheme else {
    val context = LocalContext.current
    ExpenseSyncColorScheme(
        lightColorScheme = isApiGreaterThenS(run = {
        dynamicLightColorScheme(context)
    }, runElse = {
        defaultColorScheme.lightColorScheme
    }), darkColorScheme = isApiGreaterThenS(run = {
        dynamicDarkColorScheme(context)
    }, runElse = {
        defaultColorScheme.darkColorScheme
    }))
}


private fun <T> isApiGreaterThenS(
    run: () -> T, runElse: () -> T
) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) run.invoke() else runElse.invoke()