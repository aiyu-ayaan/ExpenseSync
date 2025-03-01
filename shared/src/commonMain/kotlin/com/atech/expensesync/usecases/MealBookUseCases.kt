package com.atech.expensesync.usecases

import com.atech.expensesync.database.room.meal.MealBook
import com.atech.expensesync.database.room.meal.MealBookEntry
import com.atech.expensesync.database.room.meal.MealDao

data class MealBookUseCases(
    val createMealBook: CreateMealBook,
    val createMealBookEntry: CreateMealBookEntry,
    val getMealBooks: GetMealBooks,
    val getMealBookEntries: GetMealBookEntries,
    val updateMealBook: UpdateMealBook,
    val updateMealBookEntry: UpdateMealBookEntry,
    val deleteMealBook: DeleteMealBook,
    val deleteMealBookEntry: DeleteMealBookEntry
)


data class CreateMealBook(private val dao: MealDao) {
    suspend operator fun invoke(mealBook: MealBook): Long = dao.createMealBook(mealBook)
}

data class CreateMealBookEntry(private val dao: MealDao) {
    suspend operator fun invoke(mealBookEntry: MealBookEntry): Long =
        dao.createMealBookEntry(mealBookEntry)
}

data class GetMealBooks(private val dao: MealDao) {
    operator fun invoke() = dao.getMealBooks()
}

data class GetMealBookEntries(private val dao: MealDao) {
    operator fun invoke(mealBookId: String) = dao.getMealBookEntries(mealBookId)
}

data class UpdateMealBook(private val dao: MealDao) {
    suspend operator fun invoke(mealBook: MealBook) = dao.updateMealBook(mealBook)
}

data class UpdateMealBookEntry(private val dao: MealDao) {
    suspend operator fun invoke(mealBookEntry: MealBookEntry) =
        dao.updateMealBookEntry(mealBookEntry)
}

data class DeleteMealBook(private val dao: MealDao) {
    suspend operator fun invoke(mealBook: MealBook) = dao.deleteMealBook(mealBook)
}

data class DeleteMealBookEntry(private val dao: MealDao) {
    suspend operator fun invoke(mealBookEntry: MealBookEntry) =
        dao.deleteMealBookEntry(mealBookEntry)
}