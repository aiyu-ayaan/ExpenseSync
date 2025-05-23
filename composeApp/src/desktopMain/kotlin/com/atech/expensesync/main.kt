package com.atech.expensesync

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.atech.expensesync.koin.KoinInitializer
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.atech.expensesync.ui_utils.lifecycler.LocalWindowState
import com.atech.expensesync.ui_utils.lifecycler.rememberLifecycleRegistry
import com.atech.expensesync.ui_utils.lifecycler.trackDesktopWindowEvents
import org.koin.core.context.stopKoin
import java.awt.Dimension
import java.awt.Toolkit

fun main() = application {
    var isKoinInitialized by remember { mutableStateOf(false) }

    if (!isKoinInitialized) {
        try {
            KoinInitializer().init()
            isKoinInitialized = true
        } catch (e: Exception) {
            println("Koin initialization error: ${e.message}")
        }
    }
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val windowState = rememberWindowState(
        size = calculateWindowSize(screenSize)
    )
    CompositionLocalProvider(LocalWindowState provides windowState) {
        ExpenseSyncTheme {
            Window(
                state = windowState,
                onCloseRequest = {
                    if (isKoinInitialized) {
                        stopKoin()
                    }
                    exitApplication()
                },
                title = "Research Hub",
                /*icon = painterResource(Res.drawable.app_logo),*/
            ) {
                val lifecycleRegistry = rememberLifecycleRegistry()
                trackDesktopWindowEvents(lifecycleRegistry)
                App(
                )
            }
        }
    }
}

fun calculateWindowSize(screenSize: Dimension): DpSize {
    val preferredWidth = 1000.dp
    val preferredHeight = 800.dp
    val minWidth = 800.dp
    val minHeight = 600.dp
    val maxWidthRatio = 0.8f
    val maxHeightRatio = 0.8f

    val screenWidth = screenSize.width.dp
    val screenHeight = screenSize.height.dp

    val width = preferredWidth.coerceIn(minWidth, screenWidth * maxWidthRatio)
    val height = preferredHeight.coerceIn(minHeight, screenHeight * maxHeightRatio)

    return DpSize(width, height)
}