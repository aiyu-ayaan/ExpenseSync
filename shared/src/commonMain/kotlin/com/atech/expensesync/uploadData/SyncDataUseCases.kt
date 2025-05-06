package com.atech.expensesync.uploadData

import com.atech.expensesync.database.models.toExpenseBook
import com.atech.expensesync.database.models.toExpenseBookEntry
import com.atech.expensesync.database.models.toMealBook
import com.atech.expensesync.database.models.toMealBookEntry
import com.atech.expensesync.database.room.expense.ExpenseBookDao
import com.atech.expensesync.database.room.expense.ExpenseBookEntry
import com.atech.expensesync.database.room.meal.MealDao
import com.atech.expensesync.firebase.usecase.GetExpenseDataUseCases
import com.atech.expensesync.firebase.usecase.GetMealBookDataUseCases
import com.atech.expensesync.firebase.util.FirebaseResponse
import com.atech.expensesync.utils.networkFetchData
import kotlinx.coroutines.flow.Flow


data class MealBookSyncUseCases(
    val mealBookDataSync: MealBookDataSyncUseCases,
    val mealBookEntryDataSync: MealBookEntryDataSyncUseCases,
)

data class MealBookDataSyncUseCases(
    private val dao: MealDao, private val getMealBookDataUseCases: GetMealBookDataUseCases
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
            val remoteIds = mealBook.meal_book.map { it.mealBookId }
            val remoteMealBooks = mealBook.meal_book.map { it.toMealBook() }
            dao.createMealBook(remoteMealBooks)
            dao.deleteMealBookOtherThanIds(remoteIds)
        },
        isDataDifferent = { mealBook ->
            val localMealBooks = dao.getMealBooksSync() // Need to add this method to your DAO
            val remoteMealBooks = mealBook.meal_book.map { it.toMealBook() }

            // Compare if the data sets are different
            val isDifferent = !compareCollections(localMealBooks, remoteMealBooks)
            isDifferent
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
            dao.getMealBookEntries()
        },
        fetch = {
            getMealBookDataUseCases.getMealBookEntryData(uid)
        },
        saveFetchResult = { mealBook ->
            val remoteEntries = mealBook.meal_book_entry.map { it.toMealBookEntry() }
            val mealBookIds = remoteEntries.map { it.mealBookId }.distinct()
            dao.createMealBookEntry(remoteEntries)
            dao.deleteMealBookEntryOtherThanIds(mealBookIds)
        },
        isDataDifferent = { mealBook ->
            val localEntries = dao.getMealBookEntriesSync() // Need to add this method to your DAO
            val remoteEntries = mealBook.meal_book_entry.map { it.toMealBookEntry() }

            val isDifferent = !compareCollections(localEntries, remoteEntries)
            isDifferent
        }
    )
}


data class ExpenseBookSyncUseCases(
    val expenseBookDataSyncUseCase: ExpenseBookDataSyncUseCase,
    val expenseBookDataEntrySyncUseCase: ExpenseBookDataEntrySyncUseCase
)

data class ExpenseBookDataSyncUseCase(
    private val dao: ExpenseBookDao,
    private val getExpenseDataUseCases: GetExpenseDataUseCases
) {
    operator fun invoke(
        uid: String
    ) = networkFetchData(
        query = {
            dao.getAllExpenses()
        },
        fetch = {
            getExpenseDataUseCases.getExpenseBookData(uid)
        },
        saveFetchResult = { expenseBook ->
            val remoteIds = expenseBook.expense_book.map { it.bookId }
            val remoteExpenseBooks = expenseBook.expense_book.map { it.toExpenseBook() }
            dao.insertExpense(remoteExpenseBooks)
            dao.deleteExpenseBookOtherThanIds(remoteIds)
        },
        isDataDifferent = { expensesData ->
            val localExpenseBooks =
                dao.getAllExpensesUpload() // Need to add this method to your DAO
            val remoteExpenseBooks = expensesData.expense_book
                .map { it.toExpenseBook() }

            val isDifferent = !compareCollections(localExpenseBooks, remoteExpenseBooks)
            isDifferent
        }
    )
}


data class ExpenseBookDataEntrySyncUseCase(
    private val dao: ExpenseBookDao,
    private val getExpenseDataUseCases: GetExpenseDataUseCases
) {
    operator fun invoke(
        uid: String
    ): Flow<FirebaseResponse<List<ExpenseBookEntry>>> = networkFetchData(
        query = {
            dao.getExpenseBookEntry()
        },
        fetch = {
            getExpenseDataUseCases.getExpenseBookEntryData(uid)
        },
        saveFetchResult = { expenseBook ->
            val remoteEntries = expenseBook.expense_book_entry.map { it.toExpenseBookEntry() }
            val mealBookIds = remoteEntries.map { it.createdAt }.distinct()
            dao.insertExpenseEntry(remoteEntries)
            dao.deleteExpenseBookEntryOtherThanIds(mealBookIds)
        },
        isDataDifferent = { expensesData ->
            val localEntries =
                dao.getAllExpensesUpload() // Need to add this method to your DAO
            val remoteEntries = expensesData.expense_book_entry
                .map { it.toExpenseBookEntry() }

            val isDifferent = !compareCollections(localEntries, remoteEntries)
            isDifferent
        }
    )
}


private fun <T> compareCollections(local: List<T>, remote: List<T>): Boolean {
    if (local.size != remote.size) return false
    val localMap = local.groupBy { it.hashCode() }.mapValues { it.value.size }
    val remoteMap = remote.groupBy { it.hashCode() }.mapValues { it.value.size }

    return localMap == remoteMap
}