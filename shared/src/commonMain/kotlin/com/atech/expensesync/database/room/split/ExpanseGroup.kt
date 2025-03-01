package com.atech.expensesync.database.room.split

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.atech.expensesync.utils.Currency
import com.atech.expensesync.utils.convertToDateFormat
import com.atech.expensesync.utils.removeDecimalIfZero
import java.util.UUID

@Keep
enum class SplitType {
    EQUAL,          // Split equally among all members
    UNEQUAL,        // Different amounts for different members
    PERCENTAGE,     // Split based on percentage contributions
    SHARES,         // Split based on predefined shares
    EXACT           // Precise amount for each member
}


@Entity(
    tableName = "expanse_group_members",
    indices = [
        Index(value = ["email"]),
        Index(value = ["groupId"]),
        Index(value = ["key"]),
    ],
    foreignKeys = [ForeignKey(
        entity = ExpanseGroup::class,
        parentColumns = ["groupId"],
        childColumns = ["groupId"],
        onDelete = CASCADE
    )]
)
@Keep
data class ExpenseGroupMembers(
    val uid: String,
    val name: String,
    val email: String,
    val pic: String,
    val groupId: String,
    val addedAt: Long = System.currentTimeMillis(),
    @PrimaryKey val key: String = "$uid$$groupId",
) {
    @get:Ignore
    val formatedDate: String
        get() = addedAt.convertToDateFormat()
}


@Keep
@Entity(
    tableName = "expanse_transactions",
    indices = [
        Index(value = ["transactionId"], unique = true),
        Index(value = ["groupId"]),
        Index(value = ["paidByKey"]),
    ],
    foreignKeys = [ForeignKey(
        entity = ExpanseGroup::class,
        parentColumns = ["groupId"],
        childColumns = ["groupId"],
        onDelete = CASCADE
    ), ForeignKey(
        entity = ExpenseGroupMembers::class,
        parentColumns = ["key"],
        childColumns = ["paidByKey"],
        onDelete = CASCADE
    )]
)
data class ExpenseTransactions(
    @PrimaryKey val transactionId: String = UUID.randomUUID().toString(),
    val groupId: String,
    val amount: Double,
    val description: String,
    val paidByKey: String,
    val splitType: SplitType = SplitType.EQUAL,
    val category: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val isSettled: Boolean = false,
) {
    @get:Ignore
    val formatedDate: String
        get() = createdAt.convertToDateFormat()

    val formatedAmount: String
        get() = amount.removeDecimalIfZero()
}

@Keep
@Entity(
    tableName = "expanse_transaction_split",
    indices = [
        Index(value = ["transactionId"]),
        Index(value = ["memberKey"]),
    ],
    foreignKeys = [ForeignKey(
        entity = ExpenseTransactions::class,
        parentColumns = ["transactionId"],
        childColumns = ["transactionId"],
        onDelete = CASCADE
    ), ForeignKey(
        entity = ExpenseGroupMembers::class,
        parentColumns = ["key"],
        childColumns = ["memberKey"],
        onDelete = CASCADE
    )]
)
data class TransactionSplit(
    val transactionId: String,
    val memberKey: String,
    val amount: Double,
    val paidBy: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isSettled: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
) {
    @get:Ignore
    val formatedDate: String
        get() = createdAt.convertToDateFormat()

    val formatedAmount: String
        get() = amount.removeDecimalIfZero()
}


@Keep
@Entity(
    tableName = "expanse_group",
    indices = [Index(value = ["groupName"], unique = true)],
)
data class ExpanseGroup(
    @PrimaryKey val groupId: String,
    val groupName: String,
    val type: String,
    val createdByUid: String,
    val defaultSplitType: SplitType = SplitType.EQUAL,
    val whiteBoard: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
    val defaultCurrency: Currency = Currency.INR,
) {
    @get:Ignore
    val formatedDate: String
        get() = createdAt.convertToDateFormat()
}
