package com.atech.expensesync.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

internal val lightColorScheme = ColorScheme(
    background = Color(0xfffcf9f7),
    error = Color(0xffba1a1a),
    errorContainer = Color(0xffffdad6),
    inverseOnSurface = Color(0xfff3f0ef),
    inversePrimary = Color(0xffc0c9c2),
    inverseSurface = Color(0xff313030),
    onBackground = Color(0xff1c1b1b),
    onError = Color(0xffffffff),
    onErrorContainer = Color(0xff410002),
    onPrimary = Color(0xffffffff),
    onPrimaryContainer = Color(0xff404943),
    onSecondary = Color(0xffffffff),
    onSecondaryContainer = Color(0xff464745),
    onSurface = Color(0xff1c1b1b),
    onSurfaceVariant = Color(0xff474746),
    onTertiary = Color(0xffffffff),
    onTertiaryContainer = Color(0xff444749),
    outline = Color(0xff787776),
    outlineVariant = Color(0xffcac9c6),
    primary = Color(0xff57605b),
    primaryContainer = Color(0xffdce5dd),
    scrim = Color(0xff000000),
    secondary = Color(0xff5d5f5d),
    secondaryContainer = Color(0xffe3e3e0),
    surface = Color(0xfffcf9f7),
    surfaceTint = Color(0xff57605b),
    surfaceVariant = Color(0xffe5e2e1),
    tertiary = Color(0xff5c5f60),
    tertiaryContainer = Color(0xffe1e2e4),
    surfaceBright = Color(0xfffcf9f7),
    surfaceDim = Color(0xffdcd9d8),
    surfaceContainer = Color(0xfff0edec),
    surfaceContainerHigh = Color(0xffebe7e6),
    surfaceContainerHighest = Color(0xffe5e2e1),
    surfaceContainerLow = Color(0xfff6f3f2),
    surfaceContainerLowest = Color(0xffffffff),
)

internal val darkColorScheme = ColorScheme(
    background = Color(0xff131313),
    error = Color(0xffffb4ab),
    errorContainer = Color(0xff93000a),
    inverseOnSurface = Color(0xff313030),
    inversePrimary = Color(0xff57605b),
    inverseSurface = Color(0xffe5e2e1),
    onBackground = Color(0xffe5e2e1),
    onError = Color(0xff690005),
    onErrorContainer = Color(0xffffdad6),
    onPrimary = Color(0xff2a322d),
    onPrimaryContainer = Color(0xffdce5dd),
    onSecondary = Color(0xff2f312f),
    onSecondaryContainer = Color(0xffe3e3e0),
    onSurface = Color(0xffe5e2e1),
    onSurfaceVariant = Color(0xffc8c6c5),
    onTertiary = Color(0xff2e3132),
    onTertiaryContainer = Color(0xffe1e2e4),
    outline = Color(0xff929090),
    outlineVariant = Color(0xff4b4c4b),
    primary = Color(0xffc0c9c2),
    primaryContainer = Color(0xff404943),
    scrim = Color(0xff000000),
    secondary = Color(0xffc6c7c4),
    secondaryContainer = Color(0xff464745),
    surface = Color(0xff131313),
    surfaceTint = Color(0xffc0c9c2),
    surfaceVariant = Color(0xff474746),
    tertiary = Color(0xffc5c7c8),
    tertiaryContainer = Color(0xff444749),
    surfaceBright = Color(0xff3a3938),
    surfaceDim = Color(0xff131313),
    surfaceContainer = Color(0xff20201f),
    surfaceContainerHigh = Color(0xff2a2a29),
    surfaceContainerHighest = Color(0xff353534),
    surfaceContainerLow = Color(0xff1c1b1b),
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