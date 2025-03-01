package com.atech.expensesync.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
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
        MealBookEntry::class
    ],
    version = 2
)
abstract class ExpenseSyncDatabase : RoomDatabase() {
    abstract val expanseGroupDao: ExpanseGroupDao
    abstract val expanseGroupMemberDao: ExpanseGroupMemberDao
    abstract val expanseTransactionDao: ExpanseTransactionDao
    abstract val transactionSplitDao: TransactionSplitDao
    abstract val mealDao: MealDao
}