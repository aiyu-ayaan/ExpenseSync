package com.atech.expensesync.usecases

import com.atech.expensesync.database.models.toMealBook
import com.atech.expensesync.database.models.toMealBookEntry
import com.atech.expensesync.database.room.meal.MealDao
import com.atech.expensesync.firebase.usecase.GetMealBookDataUseCases
import com.atech.expensesync.utils.expenseSyncLogger
import com.atech.expensesync.utils.networkFetchData


data class MealBookSyncUseCases(
    val mealBookDataSync: MealBookDataSyncUseCases,
    val mealBookEntryDataSync: MealBookEntryDataSyncUseCases,
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

data class MealBookEntryDataSyncUseCases(
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
            getMealBookDataUseCases.getMealBookEntryData(uid)
        },
        saveFetchResult = { mealBook ->
            expenseSyncLogger(
                "Called saveFetchResult ${mealBook.meal_book_entry}",
            )
            dao.createMealBookEntry(mealBook.meal_book_entry.map { it.toMealBookEntry() })
        }
    )
}