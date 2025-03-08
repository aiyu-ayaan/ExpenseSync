package com.atech.expensesync.database.room

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File

fun getExpenseSyncDatabase(): SplitSyncDatabase {
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
    return Room.databaseBuilder<SplitSyncDatabase>(
        name = dbFile.absolutePath,
    ).setDriver(BundledSQLiteDriver())
        .addMigrations(SplitSyncDatabase.MIGRATION_1_2)
        .fallbackToDestructiveMigration(false)
        .build()
}