package com.atech.expensesync.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.atech.expensesync.database.room.meal.MealBook
import com.atech.expensesync.database.room.meal.MealBookEntry
import com.atech.expensesync.database.room.meal.MealDao
import com.atech.expensesync.database.room.split.ExpanseGroup
import com.atech.expensesync.database.room.split.ExpanseGroupDao
import com.atech.expensesync.database.room.split.ExpanseGroupMemberDao
import com.atech.expensesync.database.room.split.ExpanseTransactionDao
import com.atech.expensesync.database.room.split.ExpenseGroupMembers
import com.atech.expensesync.database.room.split.ExpenseTransactions
import com.atech.expensesync.database.room.split.TransactionSplit
import com.atech.expensesync.database.room.split.TransactionSplitDao


@Database(
    entities = [
        ExpenseGroupMembers::class,
        ExpenseTransactions::class,
        TransactionSplit::class,
        ExpanseGroup::class,
        MealBook::class,
        MealBookEntry::class,
    ],
    version = 3
)
abstract class ExpenseSyncDatabase : RoomDatabase() {
    abstract val expanseGroupDao: ExpanseGroupDao
    abstract val expanseGroupMemberDao: ExpanseGroupMemberDao
    abstract val expanseTransactionDao: ExpanseTransactionDao
    abstract val transactionSplitDao: TransactionSplitDao
    abstract val mealDao: MealDao

    companion object {
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(connection: SQLiteConnection) {
                connection.execSQL(
                    "CREATE TABLE IF NOT EXISTS `meal_book` (`bookId` TEXT NOT NULL, `bookName` TEXT NOT NULL, `description` TEXT NOT NULL, `totalAmount` REAL NOT NULL, `totalIn` REAL NOT NULL, `totalOut` REAL NOT NULL, `defaultCurrency` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`bookId`))"
                )
                connection.execSQL(
                    "CREATE TABLE IF NOT EXISTS `meal_book_entry` (`entryId` TEXT NOT NULL, `bookId` TEXT NOT NULL, `entryName` TEXT NOT NULL, `entryAmount` REAL NOT NULL, `entryCurrency` TEXT NOT NULL, `entryDate` INTEGER NOT NULL, `entryType` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`entryId`))"
                )
            }
        }
    }
}