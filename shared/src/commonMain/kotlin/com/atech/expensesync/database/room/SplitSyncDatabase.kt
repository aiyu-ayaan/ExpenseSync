package com.atech.expensesync.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.atech.expensesync.database.room.expense.ExpenseBookDao
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


@Database(
    entities = [
        SplitGroupMembers::class,
        SplitTransactions::class,
        TransactionSplit::class,
        SplitGroup::class,
        MealBook::class,
        MealBookEntry::class,
    ],
    version = 1
)
abstract class SplitSyncDatabase : RoomDatabase() {
    abstract val splitGroupDao: SplitGroupDao
    abstract val splitGroupMemberDao: SplitGroupMemberDao
    abstract val splitTransactionDao: SplitTransactionDao
    abstract val transactionSplitDao: TransactionSplitDao
    abstract val mealDao: MealDao
    abstract val expenseBookDao: ExpenseBookDao
}