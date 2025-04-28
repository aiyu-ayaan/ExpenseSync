package com.atech.expensesync.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.atech.expensesync.database.room.expense.ExpenseBook
import com.atech.expensesync.database.room.expense.ExpenseBookDao
import com.atech.expensesync.database.room.expense.ExpenseBookEntry
import com.atech.expensesync.database.room.meal.MealBook
import com.atech.expensesync.database.room.meal.MealBookEntry
import com.atech.expensesync.database.room.meal.MealDao
import com.atech.expensesync.database.room.split.SplitGroup
import com.atech.expensesync.database.room.split.SplitGroupDao
import com.atech.expensesync.database.room.split.SplitGroupMemberDao
import com.atech.expensesync.database.room.split.SplitGroupMembers
import com.atech.expensesync.database.room.split.SplitTransactionDao
import com.atech.expensesync.database.room.split.SplitTransactions
import com.atech.expensesync.database.room.split.TransactionSplit
import com.atech.expensesync.database.room.split.TransactionSplitDao
import com.atech.expensesync.database.room.splitv2.GroupMembers
import com.atech.expensesync.database.room.splitv2.SplitDao
import com.atech.expensesync.database.room.splitv2.SplitGlobalTransactions
import com.atech.expensesync.database.room.splitv2.SplitModel
import com.atech.expensesync.database.room.upload.UploadDao
import com.atech.expensesync.database.room.upload.UploadModel


@Database(
    entities = [ MealBook::class,
        MealBookEntry::class, ExpenseBook::class, ExpenseBookEntry::class,
        UploadModel::class, GroupMembers::class, SplitGlobalTransactions::class,
        com.atech.expensesync.database.room.splitv2.SplitTransactions::class,
        SplitModel::class
    ],
    version = 8
)
abstract class SplitSyncDatabase : RoomDatabase() {
//    abstract val splitGroupDao: SplitGroupDao
//    abstract val splitGroupMemberDao: SplitGroupMemberDao
//    abstract val splitTransactionDao: SplitTransactionDao
//    abstract val transactionSplitDao: TransactionSplitDao
    abstract val mealDao: MealDao
    abstract val expenseBookDao: ExpenseBookDao
    abstract val updateDao: UploadDao
    abstract val maintenanceDao: MaintenanceDao
    abstract val splitDao : SplitDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(connection: SQLiteConnection) {
                connection.execSQL(
                    """
                        ALTER TABLE meal_book ADD COLUMN icon TEXT NOT NULL DEFAULT ""
                    """.trimIndent()
                )
                connection.execSQL(
                    """
                        ALTER TABLE expense_book ADD COLUMN icon TEXT NOT NULL DEFAULT ""
                    """.trimIndent()
                )
            }
        }
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(connection: SQLiteConnection) {
                connection.execSQL(
                    """
                        ALTER TABLE expense_book ADD COLUMN updatedAt INTEGER NULL DEFAULT NULL
                    """.trimIndent()
                )
            }
        }
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(connection: SQLiteConnection) {
                connection.execSQL(
                    """
                        ALTER TABLE expense_book_entry ADD COLUMN paymentMethod TEXT NOT NULL DEFAULT "CASH"
                    """.trimIndent()
                )
            }
        }
        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(connection: SQLiteConnection) {
                connection.execSQL(
                    """
                        CREATE TABLE IF NOT EXISTS `upload_table` (
                            `updatedType` TEXT NOT NULL,
                            `isUpdated` INTEGER NOT NULL DEFAULT 0,
                            `updatedTime` INTEGER NOT NULL DEFAULT 0,
                            `createdAt` INTEGER NOT NULL DEFAULT 0,
                            PRIMARY KEY(`createdAt`)
                        )
                    """.trimIndent()
                )
            }
        }
    }
}