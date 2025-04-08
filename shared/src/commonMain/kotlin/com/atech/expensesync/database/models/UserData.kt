package com.atech.expensesync.database.models

import androidx.annotation.Keep
import com.atech.expensesync.database.room.meal.MealBookEntry
import com.atech.expensesync.utils.Currency
import java.util.UUID

@Keep
data class MealBookEntryFirebase(
    val price: Double = 0.0,
    val description: String = "",
    val mealBookId: String = "",
    val createdAt: Long = System.currentTimeMillis(),
)

@Keep
data class MealBookFirebaseList(
    val meal_book_entry: List<MealBookEntryFirebase> = emptyList(),
)

@Keep
fun MealBookEntryFirebase.toMealBookEntry(): MealBookEntry = MealBookEntry(
    price = price,
    description = description,
    mealBookId = mealBookId,
    createdAt = createdAt,
)

@Keep
data class MealBookEntryFirebaseList(
    val meal_book: List<MealBookFirebase> = emptyList(),
)

@Keep
data class MealBookFirebase(
    val name: String = "",
    var icon: String = "",
    val defaultPrice: Double = 0.0,
    val defaultCurrency: Currency = Currency.INR,
    val description: String = "",
    val created: Long = System.currentTimeMillis(),
    val mealBookId: String = UUID.randomUUID().toString(),
)

fun MealBookFirebase.toMealBook(): com.atech.expensesync.database.room.meal.MealBook =
    com.atech.expensesync.database.room.meal.MealBook(
        name = name,
        icon = icon,
        defaultPrice = defaultPrice,
        defaultCurrency = defaultCurrency,
        description = description,
        created = created,
        mealBookId = mealBookId,
    )