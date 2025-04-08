package com.atech.expensesync.usecases

import com.atech.expensesync.database.models.toMealBook
import com.atech.expensesync.database.room.meal.MealDao
import com.atech.expensesync.firebase.usecase.GetMealBookDataUseCases
import com.atech.expensesync.utils.expenseSyncLogger
import com.atech.expensesync.utils.networkFetchData


data class MealBookSyncUseCases(
    val mealBookDataSync: MealBookDataSyncUseCases
)

data class MealBookDataSyncUseCases(
    private val dao: MealDao,
    private val getMealBookDataUseCases: GetMealBookDataUseCases
) {
    operator fun invoke(
        uid: String
    ) = networkFetchData(
        query = {
            dao.getMealBooks()
        },
        fetch = {
            getMealBookDataUseCases.getMealBookData(uid)
        },
        saveFetchResult = { mealBook ->
            expenseSyncLogger(
                "Called saveFetchResult ${mealBook.meal_book}",
            )
            dao.createMealBook(mealBook.meal_book.map { it.toMealBook() })
        }
    )
}