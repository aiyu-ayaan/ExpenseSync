package com.atech.expensesync.ui_utils.lifecycler

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
actual fun rememberLifecycleRegistry(): LifecycleRegistry {
    val lifecycleRegistry = remember { LifecycleRegistry() }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = object : androidx.lifecycle.DefaultLifecycleObserver {
            override fun onCreate(owner: androidx.lifecycle.LifecycleOwner) {
                lifecycleRegistry.setLifecycleState(LifeCycle.ON_CREATE)
            }

            override fun onStart(owner: androidx.lifecycle.LifecycleOwner) {
                lifecycleRegistry.setLifecycleState(LifeCycle.ON_START)
            }

            override fun onResume(owner: androidx.lifecycle.LifecycleOwner) {
                lifecycleRegistry.setLifecycleState(LifeCycle.ON_RESUME)
            }

            override fun onPause(owner: androidx.lifecycle.LifecycleOwner) {
                lifecycleRegistry.setLifecycleState(LifeCycle.ON_PAUSE)
            }

            override fun onStop(owner: androidx.lifecycle.LifecycleOwner) {
                lifecycleRegistry.setLifecycleState(LifeCycle.ON_STOP)
            }

            override fun onDestroy(owner: androidx.lifecycle.LifecycleOwner) {
                lifecycleRegistry.setLifecycleState(LifeCycle.ON_DESTROY)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    return lifecycleRegistry
}