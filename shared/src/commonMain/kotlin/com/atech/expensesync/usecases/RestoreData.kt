package com.atech.expensesync.usecases

import com.atech.expensesync.database.models.MealBookEntryFirebaseList
import com.atech.expensesync.database.models.MealBookFirebaseList
import com.atech.expensesync.database.models.toMealBook
import com.atech.expensesync.database.models.toMealBookEntry
import com.atech.expensesync.database.room.meal.MealDao
import com.atech.expensesync.firebase.util.getException
import com.atech.expensesync.firebase.util.getOrNull
import com.atech.expensesync.firebase.util.isError
import com.atech.expensesync.firebase.util.isSuccess
import com.atech.expensesync.utils.FirebaseCollectionPath
import com.atech.expensesync.utils.FirebaseDocumentName

sealed interface BackUpState {
    data object OnSuccess : BackUpState
    data class OnError(val exception: Exception) : BackUpState
}

data class RestoreData(
    val restoreMealData: RestoreMealData
)


data class RestoreMealData(
    private val kmpFire: com.atech.expensesync.firebase.io.KmpFire,
    private val mealDoa: MealDao
) {
    suspend operator fun invoke(
        uid: String,
        status: (BackUpState) -> Unit
    ) {
        val data = kmpFire.getData<MealBookFirebaseList>(
            collectionName = FirebaseCollectionPath.USER.path + "/$uid/data",
            documentName = FirebaseDocumentName.MEAL_BOOK.path
        )
        if (data.isSuccess()) {
            val mealBooks = data.getOrNull()
            if (mealBooks != null) {
                mealDoa.createMealBook(mealBooks.meal_book.map { it.toMealBook() })
            }
        }
        if (data.isError()) {
            status(
                BackUpState.OnError(
                    data.getException() ?: Exception("Unknown error")
                )
            )
            return
        }
        val mealBookEntries = kmpFire.getData<MealBookEntryFirebaseList>(
            collectionName = FirebaseCollectionPath.USER.path + "/$uid/data",
            documentName = FirebaseDocumentName.MEAL_BOOK_ENTRY.path
        )
        if (mealBookEntries.isSuccess()) {
            val mealBookEntry = mealBookEntries.getOrNull()
            if (mealBookEntry != null) {
                mealDoa.createMealBookEntry(mealBookEntry.meal_book_entry.map { it.toMealBookEntry() })
            }
        }
        if (mealBookEntries.isError()) {
            status(
                BackUpState.OnError(
                    mealBookEntries.getException() ?: Exception("Unknown error")
                )
            )
            return
        }
        status.invoke(
            BackUpState.OnSuccess
        )
    }
}