package com.atech.expensesync.database.room.meal

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.atech.expensesync.utils.Currency
import com.atech.expensesync.utils.convertToDateFormat
import java.util.UUID

@Keep
@Entity(
    tableName = "meal_book_entry",
    indices = [
        Index(value = ["mealBookId"]),
    ],
    foreignKeys = [
        ForeignKey(
            entity = MealBook::class,
            parentColumns = ["mealBookId"],
            childColumns = ["mealBookId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MealBookEntry(
    val price: Double,
    val description: String = "",
    val mealBookId: String,
    @PrimaryKey(autoGenerate = false)
    val createdAt: Long = System.currentTimeMillis(),
) {
    @get:Ignore
    val formattedDate: String
        get() = createdAt.convertToDateFormat()
}


@Keep
@Entity(
    tableName = "meal_book",
    indices = [
        Index(value = ["name"], unique = true),
        Index(value = ["mealBookId"]),
    ],
)
data class MealBook(
    val name: String,
    val defaultPrice: Double,
    val defaultCurrency: Currency = Currency.INR,
    val description: String = "",
    val created: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = false)
    val mealBookId: String = UUID.randomUUID().toString(),
) {
    @get:Ignore
    val formattedDate: String
        get() = created.convertToDateFormat()
}

