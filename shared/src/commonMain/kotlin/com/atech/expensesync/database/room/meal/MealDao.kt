package com.atech.expensesync.database.room.meal

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createMealBook(mealBook: MealBook): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createMealBook(mealBook: List<MealBook>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createMealBookEntry(mealBookEntry: MealBookEntry): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createMealBookEntry(mealBookEntry: List<MealBookEntry>)

    @Query("SELECT * FROM meal_book ORDER BY created DESC")
    fun getMealBooks(): Flow<List<MealBook>>

    @Query("SELECT * FROM meal_book_entry WHERE mealBookId = :mealBookId ORDER BY createdAt DESC")
    fun getMealBookEntries(mealBookId: String): Flow<List<MealBookEntry>>

    @Query("SELECT * FROM meal_book")
    suspend fun getAllMealBooksUpload(): List<MealBook>

    @Query("SELECT * FROM meal_book_entry")
    suspend fun getAllMealBookEntriesUpload(): List<MealBookEntry>

    @Update
    suspend fun updateMealBook(mealBook: MealBook): Int

    @Update
    suspend fun updateMealBookEntry(mealBookEntry: MealBookEntry): Int

    @Delete
    suspend fun deleteMealBook(mealBook: MealBook)

    @Delete
    suspend fun deleteMealBookEntry(mealBookEntry: MealBookEntry)

    @Query("Delete from meal_book_entry where mealBookId = :mealBookId")
    suspend fun deleteMealBookEntries(mealBookId: String)

    @Query("Delete from meal_book where mealBookId = :mealBookId")
    suspend fun deleteMealBookById(mealBookId: String)

    @Query("DELETE FROM meal_book WHERE mealBookId NOT IN (:ids)")
    suspend fun deleteMealBookOtherThanIds(ids: List<String>)

    @Query("DELETE FROM meal_book_entry WHERE createdAt NOT IN (:ids)")
    suspend fun deleteMealBookEntryOtherThanIds(ids: List<Long>)

    @Transaction
    suspend fun deleteMealBookAndEntries(mealBookId: String) {
        deleteMealBookById(mealBookId)
        deleteMealBookEntries(mealBookId)
    }


    @Query(
        """
        SELECT SUM(price) FROM MEAL_BOOK_ENTRY 
        WHERE mealBookId = :mealBookId
        AND createdAt >= :startOfMonth
        AND createdAt <= :endOfMonth
    """
    )
    fun getTotalPrice(
        mealBookId: String, startOfMonth: Long, endOfMonth: Long
    ): Flow<Double>
}