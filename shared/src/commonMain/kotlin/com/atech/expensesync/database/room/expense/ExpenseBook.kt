package com.atech.expensesync.database.room.expense

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.atech.expensesync.utils.Currency
import java.util.UUID


enum class Category(val displayName: String) {
    NONE("None"),

    // Income Categories
    SALE("Sale"),
    BONUS("Bonus"),
    RENT_INCOME("Rent Income"),
    SALARY("Salary"),
    INTEREST("Interest"),
    DIVIDEND("Dividend"),
    INVESTMENT_PROFIT("Investment Profit"),
    FREELANCE("Freelance"),
    ROYALTY("Royalty"),
    OTHER_INCOME("Other Income"),

    // Expense Categories
    RENT("Rent"),
    UTILITIES("Utilities"),
    GROCERIES("Groceries"),
    TRANSPORT("Transportation"),
    ENTERTAINMENT("Entertainment"),
    DINING("Dining Out"),
    HEALTHCARE("Healthcare"),
    INSURANCE("Insurance"),
    EDUCATION("Education"),
    SHOPPING("Shopping"),
    TRAVEL("Travel"),
    SUBSCRIPTIONS("Subscriptions"),
    TAXES("Taxes"),
    OTHER_EXPENSES("Other Expenses");
}

enum class TransactionType {
    IN,
    OUT
}

@Entity(
    tableName = "expense_book",
    indices = [
        Index(value = ["bookId"]),
        Index(value = ["bookName"], unique = true)
    ]
)
@Keep
data class ExpenseBook(
    val bookName: String,
    val icon: String = "",
    val description: String = "",
    val totalAmount: Double = 0.0,
    val totalIn: Double = 0.0,
    val totalOut: Double = 0.0,
    val defaultCurrency: Currency = Currency.INR,
    val createdAt: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = false)
    val bookId: String = UUID.randomUUID().toString(),
)


@Entity(
    tableName = "expense_book_entry",
    indices = [
        Index(value = ["bookId"]),
    ],
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = ExpenseBook::class,
            parentColumns = ["bookId"],
            childColumns = ["bookId"],
            onDelete = androidx.room.ForeignKey.CASCADE
        )
    ]
)
@Keep
data class ExpenseBookEntry(
    val amount: Double,
    val bookId: String,
    val transactionType: TransactionType,
    val remarks: String = "",
    val category: Category = Category.NONE,
    @PrimaryKey
    val createdAt: Long = System.currentTimeMillis(),
)
