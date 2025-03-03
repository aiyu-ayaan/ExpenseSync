package com.atech.expensesync.database.room

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

fun getExpenseSyncDatabase(context: Context): SplitSyncDatabase {
    val dbFile = context.getDatabasePath("expense_sync.db")
    return Room.databaseBuilder<SplitSyncDatabase>(
        context.applicationContext,
        name = dbFile.absolutePath,
    ).setDriver(
        BundledSQLiteDriver()
    ).fallbackToDestructiveMigration(false)
        .build()
}