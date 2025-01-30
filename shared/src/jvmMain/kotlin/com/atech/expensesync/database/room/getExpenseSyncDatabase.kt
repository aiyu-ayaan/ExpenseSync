package com.atech.expensesync.database.room

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File

fun getExpenseSyncDatabase(): ExpenseSyncDatabase {
    val appDataDir = when {
        System.getProperty("os.name").contains("Windows", ignoreCase = true) ->
            System.getenv("APPDATA")

        System.getProperty("os.name").contains("Mac", ignoreCase = true) ->
            System.getProperty("user.home") + "/Library/Application Support"

        else -> // Linux and others
            System.getProperty("user.home") + "/.config"
    }
    val appFolder = File(appDataDir, "ExpenseSync")
    if (!appFolder.exists()) {
        appFolder.mkdirs()
    }
    val dbFile = File(appFolder, "expense_sync.db")
    return Room.databaseBuilder<ExpenseSyncDatabase>(
        name = dbFile.absolutePath,
    ).setDriver(BundledSQLiteDriver())
        .build()
}