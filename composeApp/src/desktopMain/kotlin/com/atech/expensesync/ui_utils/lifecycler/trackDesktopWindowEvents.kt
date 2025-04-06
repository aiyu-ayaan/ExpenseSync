package com.atech.expensesync.ui_utils.lifecycler

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@Composable
fun trackDesktopWindowEvents(lifecycleRegistry: LifecycleRegistry) {
    // This would be placed in the desktop-specific source set
    val window = java.awt.Window.getWindows().firstOrNull() ?: return

    DisposableEffect(window) {
        val windowListener = object : java.awt.event.WindowAdapter() {
            override fun windowOpened(e: java.awt.event.WindowEvent) {
                lifecycleRegistry.setLifecycleState(LifeCycle.ON_START)
                lifecycleRegistry.setLifecycleState(LifeCycle.ON_RESUME)
            }

            override fun windowIconified(e: java.awt.event.WindowEvent) {
                lifecycleRegistry.setLifecycleState(LifeCycle.ON_PAUSE)
            }

            override fun windowDeiconified(e: java.awt.event.WindowEvent) {
                lifecycleRegistry.setLifecycleState(LifeCycle.ON_RESUME)
            }

            override fun windowDeactivated(e: java.awt.event.WindowEvent) {
                lifecycleRegistry.setLifecycleState(LifeCycle.ON_PAUSE)
            }

            override fun windowActivated(e: java.awt.event.WindowEvent) {
                lifecycleRegistry.setLifecycleState(LifeCycle.ON_RESUME)
            }

            override fun windowClosing(e: java.awt.event.WindowEvent) {
                lifecycleRegistry.setLifecycleState(LifeCycle.ON_PAUSE)
                lifecycleRegistry.setLifecycleState(LifeCycle.ON_STOP)
                lifecycleRegistry.setLifecycleState(LifeCycle.ON_DESTROY)
            }
        }

        window.addWindowListener(windowListener)

        onDispose {
            window.removeWindowListener(windowListener)
        }
    }
}