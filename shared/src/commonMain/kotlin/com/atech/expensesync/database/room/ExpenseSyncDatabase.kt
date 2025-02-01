package com.atech.expensesync.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.atech.expensesync.database.room.split.ExpanseGroup
import com.atech.expensesync.database.room.split.ExpanseGroupDao
import com.atech.expensesync.database.room.split.ExpanseGroupMemberDao
import com.atech.expensesync.database.room.split.ExpanseGroupMembers
import com.atech.expensesync.database.room.split.ExpanseTransactionDao
import com.atech.expensesync.database.room.split.ExpanseTransactions
import com.atech.expensesync.database.room.split.TransactionSplit
import com.atech.expensesync.database.room.split.TransactionSplitDao


@Database(
    entities = [
        ExpanseGroupMembers::class,
        ExpanseTransactions::class,
        TransactionSplit::class,
        ExpanseGroup::class
    ],
    version = 1
)
abstract class ExpenseSyncDatabase : RoomDatabase() {
    abstract val expanseGroupDao: ExpanseGroupDao
    abstract val expanseGroupMemberDao: ExpanseGroupMemberDao
    abstract val expanseTransactionDao: ExpanseTransactionDao
    abstract val transactionSplitDao: TransactionSplitDao
}