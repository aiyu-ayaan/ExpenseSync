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
    val deleteMealBookEntry: DeleteMealBookEntry,
    val getTotalPrice: GetTotalPrice
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
    suspend operator fun invoke(mealBook: MealBook) = dao.updateMealBook(mealBook).toLong()
}

data class UpdateMealBookEntry(private val dao: MealDao) {
    suspend operator fun invoke(
        mealBookEntry: MealBookEntry,
        oldMealBookEntry: MealBookEntry? = null
    ): Long {
        if (oldMealBookEntry == null) {
            return dao.updateMealBookEntry(mealBookEntry).toLong()
        }
        if (oldMealBookEntry.createdAt != mealBookEntry.createdAt) {
            dao.deleteMealBookEntry(oldMealBookEntry)
            return dao.createMealBookEntry(mealBookEntry)
        }
        return dao.updateMealBookEntry(mealBookEntry).toLong()
    }
}

data class DeleteMealBook(private val dao: MealDao) {
    suspend operator fun invoke(mealBook: MealBook) = dao.deleteMealBook(mealBook)

    suspend operator fun invoke(mealBookId: String) = dao.deleteMealBookAndEntries(mealBookId)
}

data class DeleteMealBookEntry(private val dao: MealDao) {
    suspend operator fun invoke(mealBookEntry: MealBookEntry) =
        dao.deleteMealBookEntry(mealBookEntry)
}

data class GetTotalPrice(private val dao: MealDao) {
    operator fun invoke(
        mealBookId: String, startOfMonth: Long, endOfMonth: Long
    ) =
        dao.getTotalPrice(mealBookId, startOfMonth, endOfMonth)
}