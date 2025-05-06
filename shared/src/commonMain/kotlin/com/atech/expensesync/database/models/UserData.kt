package com.atech.expensesync.database.models

import androidx.annotation.Keep
import com.atech.expensesync.database.room.expense.Category
import com.atech.expensesync.database.room.expense.ExpenseBook
import com.atech.expensesync.database.room.expense.ExpenseBookEntry
import com.atech.expensesync.database.room.expense.PaymentMethod
import com.atech.expensesync.database.room.expense.TransactionType
import com.atech.expensesync.database.room.meal.MealBookEntry
import com.atech.expensesync.utils.Currency
import java.util.UUID

@Keep
data class MealBookEntryFirebase(
    val price: Double = 0.0,
    val description: String = "",
    val mealBookId: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    var formattedDate: String = ""
)

@Keep
data class MealBookEntryFirebaseList(
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
data class MealBookFirebaseList(
    val meal_book: List<MealBookFirebase> = emptyList(),
)

@Keep
data class MealBookFirebase(
    var name: String = "",
    var icon: String = "",
    var defaultPrice: Double = 0.0,
    var defaultCurrency: Currency = Currency.INR,
    var description: String = "",
    var created: Long = System.currentTimeMillis(),
    var mealBookId: String = UUID.randomUUID().toString(),
    var formattedDate: String = ""
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

@Keep
data class ExpenseBookFirebase(
    val bookName: String = "",
    val icon: String = "",
    val description: String = "",
    val totalAmount: Double = 0.0,
    val totalIn: Double = 0.0,
    val totalOut: Double = 0.0,
    val defaultCurrency: Currency = Currency.INR,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long? = null,
    val bookId: String = UUID.randomUUID().toString(),
    val netBalance: Double = 0.0
)

fun ExpenseBookFirebase.toExpenseBook() =
    ExpenseBook(
        bookName = bookName,
        icon = icon,
        description = description,
        totalAmount = totalAmount,
        totalIn = totalIn,
        totalOut = totalOut,
        defaultCurrency = defaultCurrency,
        createdAt = createdAt,
        updatedAt = updatedAt,
        bookId = bookId,
    )


@Keep
data class ExpenseBookFirebaseList(
    val expense_book: List<ExpenseBookFirebase> = emptyList(),
)

@Keep
data class ExpenseBookEntryFirebase(
    val amount: Double = 0.0,
    val bookId: String = "",
    val transactionType: TransactionType = TransactionType.IN,
    val remarks: String = "",
    val category: Category = Category.NONE,
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val netBalance: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis(),
)

fun ExpenseBookEntryFirebase.toExpenseBookEntry() =
    ExpenseBookEntry(
        amount = amount,
        bookId = bookId,
        transactionType = transactionType,
        remarks = remarks,
        category = category,
        paymentMethod = paymentMethod,
        netBalance = netBalance,
        createdAt = createdAt,
    )


@Keep
data class ExpenseBookEntryFirebaseList(
    val expense_book_entry: List<ExpenseBookEntryFirebase> = emptyList(),
)
