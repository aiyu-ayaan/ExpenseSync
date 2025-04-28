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



    @Query("DELETE FROM upload_table")
    suspend fun deleteAllUpload()


    @Transaction
    suspend fun deleteAll() {
        deleteAllMealBook()
        deleteAllMealBookEntry()
        deleteAllExpenseBook()
        deleteAllExpenseBookEntry()
        deleteAllUpload()
    }

}