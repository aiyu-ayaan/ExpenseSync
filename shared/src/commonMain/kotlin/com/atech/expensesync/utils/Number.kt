package com.atech.expensesync.utils

fun Double.takeUpToTwoDecimal(): Double {
    return "%.2f".format(this).toDouble()
}

fun Double.removeDecimalIfZero(): String =
    if (this % 1.0 == 0.0) {
        this.toInt().toString()
    } else {
        this.toString()
    }