package com.atech.expensesync.firebase.usecase

import com.atech.expensesync.database.room.meal.MealDao
import com.atech.expensesync.firebase.io.KmpFire
import com.atech.expensesync.firebase.util.FirebaseResponse
import com.atech.expensesync.utils.FirebaseCollectionPath
import com.atech.expensesync.utils.FirebaseDocumentName


class MealBookUploadUseCase(
    private val dao: MealDao, private val kmpFire: KmpFire
) {
    suspend operator fun invoke(
        userUid: String
    ): FirebaseResponse<Any> {
        val mealBooks = dao.getAllMealBooksUpload()
        val mealBookEntries = dao.getAllMealBookEntriesUpload()
        if (mealBooks.isEmpty()) {
            return FirebaseResponse.Error("No meal books to upload")
        }
        var state: FirebaseResponse<Any> = FirebaseResponse.Loading
        state = kmpFire.updateDataModel(
            FirebaseCollectionPath.USER.path + "/$userUid/data",
            FirebaseDocumentName.MEAL_BOOK.path,
            mealBooks.toMap(FirebaseDocumentName.MEAL_BOOK.path)
        )
        if (state is FirebaseResponse.Error) {
            return state
        }
        state = kmpFire.updateDataModel(
            FirebaseCollectionPath.USER.path + "/$userUid/data",
            FirebaseDocumentName.MEAL_BOOK_ENTRY.path,
            mealBookEntries.toMap(FirebaseDocumentName.MEAL_BOOK_ENTRY.path)
        )
        if (state is FirebaseResponse.Error) {
            return state
        }
        return FirebaseResponse.Success(Any())
    }
}

fun <T> List<T>.toMap(name: String) = mapOf(name to this)