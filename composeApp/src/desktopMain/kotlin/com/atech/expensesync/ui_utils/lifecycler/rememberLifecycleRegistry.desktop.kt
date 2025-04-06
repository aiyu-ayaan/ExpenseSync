// desktopMain/kotlin/com/atech/expensesync/ui_utils/lifecycler/LifecycleDesktop.kt

package com.atech.expensesync.ui_utils.lifecycler

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.window.WindowState
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive


val LocalWindowState = staticCompositionLocalOf<WindowState> {
    error("WindowState not provided")
}

@Composable
actual fun rememberLifecycleRegistry(): LifecycleRegistry {
    val lifecycleRegistry = remember { LifecycleRegistry() }
    val currentState = rememberUpdatedState(lifecycleRegistry)

    // This assumes you're providing windowState from the calling Window() scope
    val windowState = rememberUpdatedState(LocalWindowState.current)

    LaunchedEffect(windowState.value) {
        currentState.value.setLifecycleState(LifeCycle.ON_CREATE)
        currentState.value.setLifecycleState(LifeCycle.ON_START)
        currentState.value.setLifecycleState(LifeCycle.ON_RESUME)

        // Poll for changes (since Compose Desktop doesn't give us lifecycle directly)
        while (isActive) {
            delay(500)
            if (windowState.value.isMinimized) {
                currentState.value.setLifecycleState(LifeCycle.ON_PAUSE)
            } else {
                currentState.value.setLifecycleState(LifeCycle.ON_RESUME)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            lifecycleRegistry.setLifecycleState(LifeCycle.ON_PAUSE)
            lifecycleRegistry.setLifecycleState(LifeCycle.ON_STOP)
            lifecycleRegistry.setLifecycleState(LifeCycle.ON_DESTROY)
        }
    }

    return lifecycleRegistry
}
