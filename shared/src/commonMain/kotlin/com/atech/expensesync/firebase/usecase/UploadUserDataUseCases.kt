package com.atech.expensesync.firebase.usecase

import com.atech.expensesync.database.models.ExpenseBookEntryFirebaseList
import com.atech.expensesync.database.models.ExpenseBookFirebaseList
import com.atech.expensesync.database.models.MealBookEntryFirebaseList
import com.atech.expensesync.database.models.MealBookFirebaseList
import com.atech.expensesync.database.room.expense.ExpenseBookDao
import com.atech.expensesync.database.room.meal.MealDao
import com.atech.expensesync.firebase.helper.FirebaseHelper
import com.atech.expensesync.firebase.io.KmpFire
import com.atech.expensesync.firebase.util.FirebaseResponse
import com.atech.expensesync.utils.FirebaseCollectionPath
import com.atech.expensesync.utils.FirebaseDocumentName
import kotlinx.coroutines.flow.Flow


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

class GetMealBookDataUseCases(
    private val kmpFire: KmpFire
) {
    suspend fun getMealBookData(
        uid: String
    ): Flow<FirebaseResponse<MealBookFirebaseList>> =
        kmpFire.getObservedDataWithQuery<MealBookFirebaseList>(
            FirebaseHelper(
                collectionName = FirebaseCollectionPath.USER.path + "/$uid/data"
            ),
            FirebaseHelper(
                documentName = FirebaseDocumentName.MEAL_BOOK.path
            )
        )

    suspend fun getMealBookEntryData(
        uid: String
    ): Flow<FirebaseResponse<MealBookEntryFirebaseList>> =
        kmpFire.getObservedData<MealBookEntryFirebaseList>(
            FirebaseCollectionPath.USER.path + "/$uid/data",
            FirebaseDocumentName.MEAL_BOOK_ENTRY.path
        )
}


class UploadExpenseDataUserCase(
    private val expenseDao: ExpenseBookDao,
    private val kmpFire: KmpFire
) {

    suspend operator fun invoke(
        userId: String
    ): FirebaseResponse<Any> {
        val expenseBook = expenseDao.getAllExpensesUpload()
        val expenseBookEntry = expenseDao.getExpenseBookEntryUpload()
        if (expenseBook.isEmpty()) {
            return FirebaseResponse.Error("No expense books to upload")
        }
        var state: FirebaseResponse<Any> = FirebaseResponse.Loading
        state = kmpFire.updateDataModel(
            FirebaseCollectionPath.USER.path + "/$userId/data",
            FirebaseDocumentName.EXPENSE_BOOK.path,
            expenseBook.toMap(FirebaseDocumentName.EXPENSE_BOOK.path)
        )
        if (state is FirebaseResponse.Error) {
            return state
        }
        state = kmpFire.updateDataModel(
            FirebaseCollectionPath.USER.path + "/$userId/data",
            FirebaseDocumentName.EXPENSE_BOOK_ENTRY.path,
            expenseBookEntry.toMap(FirebaseDocumentName.EXPENSE_BOOK_ENTRY.path)
        )
        if (state is FirebaseResponse.Error) {
            return state
        }
        return FirebaseResponse.Success(Any())
    }
}


class GetAllExpenseDataUseCases(
    private val kmpFire: KmpFire
) {
    suspend fun getExpenseBookData(
        uid: String
    ): Flow<FirebaseResponse<ExpenseBookFirebaseList>> =
        kmpFire.getObservedDataWithQuery<ExpenseBookFirebaseList>(
            FirebaseHelper(
                collectionName = FirebaseCollectionPath.USER.path + "/$uid/data"
            ),
            FirebaseHelper(
                documentName = FirebaseDocumentName.EXPENSE_BOOK.path
            )
        )

    suspend fun getExpenseBookEntryData(
        uid: String
    ): Flow<FirebaseResponse<ExpenseBookEntryFirebaseList>> =
        kmpFire.getObservedDataWithQuery<ExpenseBookEntryFirebaseList>(
            FirebaseHelper(
                collectionName = FirebaseCollectionPath.USER.path + "/$uid/data"
            ),
            FirebaseHelper(
                documentName = FirebaseDocumentName.MEAL_BOOK_ENTRY.path
            )
        )
}

fun <T> List<T>.toMap(name: String) = mapOf(name to this)