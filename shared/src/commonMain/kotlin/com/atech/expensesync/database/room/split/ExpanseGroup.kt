package com.atech.expensesync.database.room.split

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.atech.expensesync.utils.convertToDateFormat
import java.util.UUID


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
    foreignKeys = [
        ForeignKey(
            entity = ExpanseGroup::class,
            parentColumns = ["groupId"],
            childColumns = ["groupId"],
            onDelete = CASCADE
        )
    ]
)
data class ExpanseGroupMembers(
    val uid: String,
    val name: String,
    val email: String,
    val pic: String,
    val groupId: String,
    val addedAt: Long = System.currentTimeMillis(),
    @PrimaryKey
    val key: String = "$uid$$groupId",
) {
    val formatedDate: String
        get() = addedAt.convertToDateFormat()
}


@Entity(
    tableName = "expanse_transactions",
    indices = [
        Index(value = ["transactionId"], unique = true),
        Index(value = ["groupId"]),
        Index(value = ["paidBy"]),
    ],
    foreignKeys = [
        ForeignKey(
            entity = ExpanseGroup::class,
            parentColumns = ["groupId"],
            childColumns = ["groupId"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = ExpanseGroupMembers::class,
            parentColumns = ["key"],
            childColumns = ["paidBy"],
            onDelete = CASCADE
        )
    ]
)
data class ExpanseTransactions(
    @PrimaryKey
    val transactionId: String = UUID.randomUUID().toString(),
    val groupId: String,
    val amount: Double,
    val description: String,
    val paidBy: String,
    val splitType: SplitType = SplitType.EQUAL,
    val category: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val isSettled: Boolean = false,
)

@Entity(
    tableName = "expanse_transaction_split",
    indices = [
        Index(value = ["transactionId"]),
        Index(value = ["memberUId"]),
    ],
    foreignKeys = [
        ForeignKey(
            entity = ExpanseTransactions::class,
            parentColumns = ["transactionId"],
            childColumns = ["transactionId"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = ExpanseGroupMembers::class,
            parentColumns = ["key"],
            childColumns = ["memberUId"],
            onDelete = CASCADE
        )
    ]
)
data class TransactionSplit(
    val transactionId: String,
    val memberUId: String,
    val amount: Double,
    val createdAt: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
) {
    val formatedDate: String
        get() = createdAt.convertToDateFormat()
}


@Entity(
    tableName = "expanse_group",
    indices = [
        Index(value = ["groupName"], unique = true)
    ],
)
data class ExpanseGroup(
    @PrimaryKey
    val groupId: String,
    val groupName: String,
    val type: String,
    val createdByUid: String,
    val defaultSplitType: SplitType = SplitType.EQUAL,
    val whiteBoard: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
    val defaultCurrency: String = "INR",
) {
    val formatedDate: String
        get() = createdAt.convertToDateFormat()
}
