package com.atech.expensesync.utils

fun Double.takeUpToTwoDecimal(): Double {
    return "%,.2f".format(this).toDouble()
}