package com.atech.expensesync.database.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface MaintenanceDao {

    @Query("DELETE FROM MEAL_BOOK")
    suspend fun deleteAllMealBook()

    @Query("DELETE FROM MEAL_BOOK_ENTRY")
    suspend fun deleteAllMealBookEntry()

    @Query("DELETE FROM EXPENSE_BOOK")
    suspend fun deleteAllExpenseBook()

    @Query("DELETE FROM EXPENSE_BOOK_ENTRY")
    suspend fun deleteAllExpenseBookEntry()

    @Query("DELETE FROM split_transactions")
    suspend fun deleteAllSplitBook()

    @Query("DELETE FROM split_group")
    suspend fun deleteAllSplitGroup()

    @Query("DELETE FROM split_group_members")
    suspend fun deleteAllSplitGroupMembers()

    @Query("DELETE FROM split_transaction_split")
    suspend fun deleteAllSplitTransactionSplit()

    @Query("DELETE FROM upload_table")
    suspend fun deleteAllUpload()


    @Transaction
    suspend fun deleteAll() {
        deleteAllMealBook()
        deleteAllMealBookEntry()
        deleteAllExpenseBook()
        deleteAllExpenseBookEntry()
        deleteAllSplitBook()
        deleteAllSplitGroup()
        deleteAllSplitGroupMembers()
        deleteAllSplitTransactionSplit()
        deleteAllUpload()
    }

}