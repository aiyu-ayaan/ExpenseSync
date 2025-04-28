package com.atech.expensesync.database.room.splitv2

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.atech.expensesync.utils.Currency
import java.util.UUID

@Keep
enum class SplitType {
    EQUAL,          // Split equally among all members
    UNEQUAL,        // Different amounts for different members
    PERCENTAGE,     // Split based on percentage contributions
    SHARES,         // Split based on predefined shares
    EXACT           // Precise amount for each member
}

@Keep
enum class Type(
    val label: String,
) {
    None(""), Home("Home"),
    Couple("Couple"),
    Trip("Trip"),
    Other("Other")
}


@Keep
@Entity(
    tableName = "splitGroupName",
    indices = [
        androidx.room.Index(value = ["groupId"]),
    ],
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = SplitModel::class,
            parentColumns = ["groupId"],
            childColumns = ["groupId"],
            onDelete = androidx.room.ForeignKey.CASCADE
        )
    ]
)
data class GroupMembers(
    @PrimaryKey
    val uid: String,
    val name: String,
    val email: String,
    val pic: String,
    val addedAt: Long = System.currentTimeMillis(),
    val groupId: String,
)


@Keep
@Entity(
    tableName = "splitGlobalTransactions",
)
data class SplitGlobalTransactions(
    val groupId: String,
    val groupMemberUid: String,
    val totalAmountOwe: Double = 0.0,
    val totalAmountPaid: Double = 0.0,
    val currency: Currency = Currency.INR,
)


@Keep
@Entity(
    tableName = "splitTransactions",
    indices = [
        androidx.room.Index(value = ["transactionId"], unique = true),
        androidx.room.Index(value = ["groupId"]),
        androidx.room.Index(value = ["paidByUid"]),
    ],
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = SplitModel::class,
            parentColumns = ["groupId"],
            childColumns = ["groupId"],
            onDelete = androidx.room.ForeignKey.CASCADE
        ),
        androidx.room.ForeignKey(
            entity = GroupMembers::class,
            parentColumns = ["uid"],
            childColumns = ["paidByUid"],
            onDelete = androidx.room.ForeignKey.CASCADE
        )
    ]
)
data class SplitTransactions(
    val transactionId: String = UUID.randomUUID().toString(),
    val groupId: String,
    val amount: Double,
    val description: String = "",
    val paidByUid: String,
    val splitType: SplitType = SplitType.EQUAL,
    val createdAt: Long = System.currentTimeMillis(),
)

@Keep
@Entity(
    "splitGroup",
    indices = [
        androidx.room.Index(value = ["groupName"], unique = true),
        androidx.room.Index(value = ["groupId"]),
    ],
)
data class SplitModel(
    val groupName: String,
    val createdByUid: String,
    val defaultCurrency: Currency = Currency.INR,
    val groupType: Type = Type.None,
    val isActive: Boolean = true,
    val whiteBoard: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    @PrimaryKey
    val groupId: String = UUID.randomUUID().toString(),
)