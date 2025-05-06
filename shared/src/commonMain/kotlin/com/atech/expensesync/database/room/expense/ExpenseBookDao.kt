package com.atech.expensesync.database.room.expense

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseBookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseBook): Long


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: List<ExpenseBook>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenseEntry(expenses: ExpenseBookEntry): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenseEntry(expenses: List<ExpenseBookEntry>): Long

    @Update
    suspend fun updateExpense(expense: ExpenseBook): Int

    @Update
    suspend fun updateExpenseEntry(expenses: ExpenseBookEntry): Int


    @Delete
    suspend fun deleteExpense(expense: ExpenseBook): Int

    @Delete
    suspend fun deleteExpenseEntry(expenses: ExpenseBookEntry): Int


    @Query("SELECT * FROM expense_book order by createdAt desc")
    fun getAllExpenses(): Flow<List<ExpenseBook>>

    @Query("SELECT * FROM expense_book order by createdAt desc")
    suspend fun getAllExpensesUpload(): List<ExpenseBook>

    @Query("SELECT * FROM expense_book where bookId = :bookId")
    fun getExpenseById(bookId: String): Flow<ExpenseBook>

    @Query("SELECT * FROM expense_book_entry where bookId = :bookId order by createdAt desc")
    fun getExpenseBookEntry(bookId: String): Flow<List<ExpenseBookEntry>>

    @Query("SELECT * FROM expense_book_entry")
    suspend fun getExpenseBookEntryUpload(): List<ExpenseBookEntry>

    @Query("SELECT * FROM expense_book_entry")
    suspend fun getExpenseBookEntry(): Flow<List<ExpenseBookEntry>>

    @Query("UPDATE expense_book SET totalAmount = totalAmount + :amount where bookId = :bookId")
    suspend fun updateTotalAmount(bookId: String, amount: Double)

    @Query("UPDATE expense_book SET totalIn = totalIn + :amount where bookId = :bookId")
    suspend fun updateTotalIn(bookId: String, amount: Double)

    @Query("UPDATE expense_book SET totalOut = totalOut + :amount where bookId = :bookId")
    suspend fun updateTotalOut(bookId: String, amount: Double)


    @Query("DELETE FROM expense_book WHERE bookId NOT IN (:ids)")
    suspend fun deleteExpenseBookOtherThanIds(ids: List<String>)

    @Query("DELETE FROM expense_book_entry WHERE createdAt NOT IN (:ids)")
    suspend fun deleteExpenseBookEntryOtherThanIds(ids: List<Long>)

    @Transaction
    suspend fun addTransaction(
        expenseBookEntry: ExpenseBookEntry
    ) {
        insertExpenseEntry(expenseBookEntry)
        if (expenseBookEntry.transactionType == TransactionType.IN) updateTotalIn(
            expenseBookEntry.bookId,
            expenseBookEntry.amount
        )
        else updateTotalOut(expenseBookEntry.bookId, expenseBookEntry.amount)
        updateTotalAmount(expenseBookEntry.bookId, expenseBookEntry.amount)
    }

}