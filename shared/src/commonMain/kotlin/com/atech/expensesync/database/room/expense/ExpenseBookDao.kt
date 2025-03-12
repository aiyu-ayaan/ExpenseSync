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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExpense(expense: ExpenseBook): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExpenseEntry(expenses: ExpenseBookEntry): Long


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

    @Query("SELECT * FROM expense_book where bookId = :bookId")
    fun getExpenseById(bookId: String): Flow<ExpenseBook>

    @Query("SELECT * FROM expense_book_entry where bookId = :bookId")
    fun getExpenseBookEntry(bookId: String): Flow<List<ExpenseBookEntry>>

    @Query("UPDATE expense_book SET totalAmount = totalAmount + :amount where bookId = :bookId")
    suspend fun updateTotalAmount(bookId: String, amount: Double)

    @Query("UPDATE expense_book SET totalIn = totalIn + :amount where bookId = :bookId")
    suspend fun updateTotalIn(bookId: String, amount: Double)

    @Query("UPDATE expense_book SET totalOut = totalOut + :amount where bookId = :bookId")
    suspend fun updateTotalOut(bookId: String, amount: Double)


    @Transaction
    suspend fun addTransaction(
        expenseBookEntry: ExpenseBookEntry
    ) {
        insertExpenseEntry(expenseBookEntry)
        if (expenseBookEntry.transactionType == TransactionType.IN)
            updateTotalIn(expenseBookEntry.bookId, expenseBookEntry.amount)
        else
            updateTotalOut(expenseBookEntry.bookId, expenseBookEntry.amount)
        updateTotalAmount(expenseBookEntry.bookId, expenseBookEntry.amount)
    }

}