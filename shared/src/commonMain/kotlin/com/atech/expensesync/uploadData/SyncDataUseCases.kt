package com.atech.expensesync.uploadData

import com.atech.expensesync.database.models.toMealBook
import com.atech.expensesync.database.models.toMealBookEntry
import com.atech.expensesync.database.room.meal.MealDao
import com.atech.expensesync.firebase.usecase.GetMealBookDataUseCases
import com.atech.expensesync.utils.networkFetchData


data class MealBookSyncUseCases(
    val mealBookDataSync: MealBookDataSyncUseCases,
    val mealBookEntryDataSync: MealBookEntryDataSyncUseCases,
)

//TODO : check here
data class MealBookDataSyncUseCases(
    private val dao: MealDao, private val getMealBookDataUseCases: GetMealBookDataUseCases
) {
    operator fun invoke(
        uid: String
    ) = networkFetchData(query = {
        dao.getMealBooks()
    }, fetch = {
        getMealBookDataUseCases.getMealBookData(uid)
    }, saveFetchResult = { mealBook ->
        val data = mealBook.meal_book.map { it.mealBookId }
        dao.createMealBook(mealBook.meal_book.map { it.toMealBook() })
        dao.deleteMealBookOtherThanIds(data)
    })
}

data class MealBookEntryDataSyncUseCases(
    private val dao: MealDao, private val getMealBookDataUseCases: GetMealBookDataUseCases
) {
    operator fun invoke(
        uid: String
    ) = networkFetchData(query = {
        dao.getMealBookEntries()
    }, fetch = {
        getMealBookDataUseCases.getMealBookEntryData(uid)
    }, saveFetchResult = { mealBook ->
        dao.createMealBookEntry(mealBook.meal_book_entry.map { it.toMealBookEntry() })
        dao.deleteMealBookEntryOtherThanIds(mealBook.meal_book_entry.map { it.mealBookId }
            .distinct())
    })
}