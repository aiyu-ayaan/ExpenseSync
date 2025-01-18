package com.atech.expensesync.utils


enum class Platform {
    JVM,
    Android
}

expect fun getPlatform(): Platform