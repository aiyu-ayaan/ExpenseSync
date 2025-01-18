package com.atech.expensesync

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform