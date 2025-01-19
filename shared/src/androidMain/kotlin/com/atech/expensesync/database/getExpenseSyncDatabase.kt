package com.atech.expensesync.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.atech.expensesync.room.ExpenseSyncDatabase

fun getExpenseSyncDatabase(context: Context): ExpenseSyncDatabase {
    val dbFile = context.getDatabasePath("expense_sync.db")
    return Room.databaseBuilder<ExpenseSyncDatabase>(
        context.applicationContext,
        name = dbFile.absolutePath,
    ).setDriver(
        BundledSQLiteDriver()
    ).build()
}