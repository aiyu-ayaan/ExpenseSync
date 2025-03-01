package com.atech.expensesync.database.room.meal

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createMealBook(mealBook: MealBook): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createMealBookEntry(mealBookEntry: MealBookEntry): Long

    @Query("SELECT * FROM meal_book ORDER BY created DESC")
    fun getMealBooks(): Flow<List<MealBook>>

    @Query("SELECT * FROM meal_book_entry WHERE mealBookId = :mealBookId ORDER BY createdAt DESC")
    fun getMealBookEntries(mealBookId: String): Flow<List<MealBookEntry>>

    @Update
    suspend fun updateMealBook(mealBook: MealBook)

    @Update
    suspend fun updateMealBookEntry(mealBookEntry: MealBookEntry)

    @Delete
    suspend fun deleteMealBook(mealBook: MealBook)

    @Delete
    suspend fun deleteMealBookEntry(mealBookEntry: MealBookEntry)
}