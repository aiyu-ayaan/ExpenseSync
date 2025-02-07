package com.atech.expensesync.ui_utils

import androidx.compose.runtime.Composable

/**
 * Device type
 */
enum class DeviceType {
    MOBILE, TABLET, DESKTOP
}

/**
 * Get display type
 * This function is used to get the display type
 * @return [DeviceType]
 */
@Composable
expect fun getDisplayType(): DeviceType