package com.atech.expensesync.ui_utils

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable

@Composable
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun <T> ThreePaneScaffoldNavigator<T>.backHandlerThreePane(
    backAction: () -> Unit = { },
    onEmptyBackStack: () -> Unit = { },
) {
    if (canNavigateBack()) {
        BackHandler(canNavigateBack()) {
            backAction()
            navigateBack()
        }
    } else {
        onEmptyBackStack()
    }
}