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
