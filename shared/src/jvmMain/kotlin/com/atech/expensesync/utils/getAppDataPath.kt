package com.atech.expensesync.utils

import java.nio.file.Paths

internal fun getAppDataPath(): String {
    val userHome = System.getProperty("user.home")
    return when {
        System.getProperty("os.name").contains("Windows", ignoreCase = true) ->
            Paths.get(System.getenv("APPDATA"), "ResearchHub").toString()

        System.getProperty("os.name").contains("Mac", ignoreCase = true) ->
            Paths.get(userHome, "Library", "Application Support", "ResearchHub").toString()

        else -> // Linux and other Unix-like systems
            Paths.get(userHome, ".config", "ResearchHub").toString()
    }
}

