package com.atech.expensesync.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

internal val lightColorScheme = ColorScheme(
    background = Color(0xfff9f9f9),
    error = Color(0xffba1a1a),
    errorContainer = Color(0xffffdad6),
    inverseOnSurface = Color(0xfff1f1f1),
    inversePrimary = Color(0xff60dbb9),
    inverseSurface = Color(0xff303030),
    onBackground = Color(0xff1b1b1b),
    onError = Color(0xffffffff),
    onErrorContainer = Color(0xff410002),
    onPrimary = Color(0xffffffff),
    onPrimaryContainer = Color(0xff002018),
    onSecondary = Color(0xffffffff),
    onSecondaryContainer = Color(0xff072019),
    onSurface = Color(0xff1b1b1b),
    onSurfaceVariant = Color(0xff474747),
    onTertiary = Color(0xffffffff),
    onTertiaryContainer = Color(0xff001e2c),
    outline = Color(0xff777777),
    outlineVariant = Color(0xffb4c2bf),
    primary = Color(0xff006b56),
    primaryContainer = Color(0xff7ef8d4),
    scrim = Color(0xff000000),
    secondary = Color(0xff4b635b),
    secondaryContainer = Color(0xffcee9dd),
    surface = Color(0xfff9f9f9),
    surfaceTint = Color(0xff006b56),
    surfaceVariant = Color(0xffe2e2e2),
    tertiary = Color(0xff416276),
    tertiaryContainer = Color(0xffc4e7ff),
    surfaceBright = Color(0xfff9f9f9),
    surfaceDim = Color(0xffdadada),
    surfaceContainer = Color(0xffeeeeee),
    surfaceContainerHigh = Color(0xffe8e8e8),
    surfaceContainerHighest = Color(0xffe2e2e2),
    surfaceContainerLow = Color(0xfff3f3f3),
    surfaceContainerLowest = Color(0xffffffff),
)

internal val darkColorScheme = ColorScheme(
    background = Color(0xff131313),
    error = Color(0xffffb4ab),
    errorContainer = Color(0xff93000a),
    inverseOnSurface = Color(0xff303030),
    inversePrimary = Color(0xff006b56),
    inverseSurface = Color(0xffe2e2e2),
    onBackground = Color(0xffe2e2e2),
    onError = Color(0xff690005),
    onErrorContainer = Color(0xffffdad6),
    onPrimary = Color(0xff00382b),
    onPrimaryContainer = Color(0xff7ef8d4),
    onSecondary = Color(0xff1d352d),
    onSecondaryContainer = Color(0xffcee9dd),
    onSurface = Color(0xffe2e2e2),
    onSurfaceVariant = Color(0xffc6c6c6),
    onTertiary = Color(0xff0d3446),
    onTertiaryContainer = Color(0xffc4e7ff),
    outline = Color(0xff919191),
    outlineVariant = Color(0xff3f4f4a),
    primary = Color(0xff60dbb9),
    primaryContainer = Color(0xff005140),
    scrim = Color(0xff000000),
    secondary = Color(0xffb2ccc2),
    secondaryContainer = Color(0xff344c43),
    surface = Color(0xff131313),
    surfaceTint = Color(0xff60dbb9),
    surfaceVariant = Color(0xff474747),
    tertiary = Color(0xffa8cbe2),
    tertiaryContainer = Color(0xff284b5e),
    surfaceBright = Color(0xff393939),
    surfaceDim = Color(0xff131313),
    surfaceContainer = Color(0xff1f1f1f),
    surfaceContainerHigh = Color(0xff2a2a2a),
    surfaceContainerHighest = Color(0xff353535),
    surfaceContainerLow = Color(0xff1b1b1b),
    surfaceContainerLowest = Color(0xff0e0e0e),
)

internal data class ExpenseSyncColorScheme(
    val lightColorScheme: ColorScheme,
    val darkColorScheme: ColorScheme
)

internal val defaultColorScheme = ExpenseSyncColorScheme(
    lightColorScheme = lightColorScheme,
    darkColorScheme = darkColorScheme
)

@Composable
internal expect fun getColorScheme(
    isEnableDynamicColor: Boolean = true
): ExpenseSyncColorScheme