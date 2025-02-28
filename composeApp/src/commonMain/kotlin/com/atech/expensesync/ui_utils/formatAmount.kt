package com.atech.expensesync.ui_utils

fun Double.formatAmount(): String =
    try {
        when {
            kotlin.math.abs(this) < 0.000001 -> ""
            this % 1.0 == 0.0 -> this.toInt().toString()
            else -> this.toString()
        }
    } catch (_: Exception) {
        ""
    }

fun String.isValidDecimalInput(maxDecimalPlaces: Int = 2): Boolean {
    return try {// Handle empty string
        if (this.isEmpty()) return true

        // Check if the input matches the decimal pattern
        if (!this.matches(Regex("^\\d*\\.?\\d*$"))) return false

        // Ensure there's only one decimal point
        if (this.count { it == '.' } > 1) return false

        // Check decimal places limit
        val parts = this.split(".")
        if (parts.size > 1 && parts[1].length > maxDecimalPlaces) return false

        true
    } catch (e: Exception) {
        false
    }
}

