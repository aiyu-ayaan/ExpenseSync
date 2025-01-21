package com.atech.expensesync.ui_utils

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember

@Composable
actual fun BackHandler(enabled: Boolean, customAction: () -> Unit) {
    val onBackCallback = remember {
        object : OnBackPressedCallback(true) {
//            override fun handleOnBackProgressed(backEvent: BackEventCompat) {
//                super.handleOnBackProgressed(backEvent)
//            }

//            override fun handleOnBackCancelled() {
//                super.handleOnBackCancelled()
//            }

            override fun handleOnBackPressed() {
                customAction.invoke()
            }
        }
    }
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner
        .current!!.onBackPressedDispatcher
    DisposableEffect(backPressedDispatcher, enabled) {
        if (enabled)
            backPressedDispatcher.addCallback(onBackCallback)
        onDispose {
            onBackCallback.remove()
        }
    }
}