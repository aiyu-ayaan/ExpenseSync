package com.atech.expensesync.usecases

import com.atech.expensesync.database.room.meal.MealBook
import com.atech.expensesync.database.room.meal.MealBookEntry
import com.atech.expensesync.database.room.meal.MealDao
import com.atech.expensesync.database.room.upload.UpdateType
import com.atech.expensesync.database.room.upload.UploadModel

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


data class CreateMealBook(
    private val dao: MealDao,
    private val insertUpload: InsertUploadUseCases
) {
    suspend operator fun invoke(mealBook: MealBook): Long {
        insertUpload.invoke(
            UploadModel(
                updatedType = UpdateType.MEAL,
                isUpdated = false,
            )
        )
        return dao.createMealBook(mealBook)
    }
}

data class CreateMealBookEntry(
    private val dao: MealDao,
    private val insertUpload: InsertUploadUseCases
) {
    suspend operator fun invoke(mealBookEntry: MealBookEntry): Long {
        insertUpload.invoke(
            UploadModel(
                updatedType = UpdateType.MEAL,
                isUpdated = false,
            )
        )
        return dao.createMealBookEntry(mealBookEntry)
    }
}

data class GetMealBooks(private val dao: MealDao) {
    operator fun invoke() = dao.getMealBooks()
}

data class GetMealBookEntries(private val dao: MealDao) {
    operator fun invoke(mealBookId: String) = dao.getMealBookEntries(mealBookId)
}

data class UpdateMealBook(
    private val dao: MealDao,
    private val insertUpload: InsertUploadUseCases
) {
    suspend operator fun invoke(mealBook: MealBook): Long {
        insertUpload.invoke(
            UploadModel(
                updatedType = UpdateType.MEAL,
                isUpdated = false,
            )
        )
        return dao.updateMealBook(mealBook).toLong()
    }
}

data class UpdateMealBookEntry(
    private val dao: MealDao,
    private val insertUpload: InsertUploadUseCases
) {
    suspend operator fun invoke(
        mealBookEntry: MealBookEntry,
        oldMealBookEntry: MealBookEntry? = null
    ): Long {
        insertUpload.invoke(
            UploadModel(
                updatedType = UpdateType.MEAL,
                isUpdated = false,
            )
        )
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

data class DeleteMealBook(
    private val dao: MealDao,
    private val insertUpload: InsertUploadUseCases
) {
    suspend operator fun invoke(mealBook: MealBook) {
        insertUpload.invoke(
            UploadModel(
                updatedType = UpdateType.MEAL,
                isUpdated = false,
            )
        )
        return dao.deleteMealBook(mealBook)
    }

    suspend operator fun invoke(mealBookId: String) {
        insertUpload.invoke(
            UploadModel(
                updatedType = UpdateType.MEAL,
                isUpdated = false,
            )
        )
        return dao.deleteMealBookAndEntries(mealBookId)
    }
}

data class DeleteMealBookEntry(
    private val dao: MealDao,
    private val insertUpload: InsertUploadUseCases
) {
    suspend operator fun invoke(mealBookEntry: MealBookEntry) {
        insertUpload.invoke(
            UploadModel(
                updatedType = UpdateType.MEAL,
                isUpdated = false,
            )
        )
        return dao.deleteMealBookEntry(mealBookEntry)
    }
}

data class GetTotalPrice(private val dao: MealDao) {
    operator fun invoke(
        mealBookId: String, startOfMonth: Long, endOfMonth: Long
    ) =
        dao.getTotalPrice(mealBookId, startOfMonth, endOfMonth)
}