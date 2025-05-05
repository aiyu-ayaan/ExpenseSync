package com.atech.expensesync.database.models

import androidx.annotation.Keep
import com.atech.expensesync.utils.Currency
import java.util.UUID

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
data class SplitFirebase(
    val groupName: String = "",
    val createdByUid: String = "",
    val defaultCurrency: Currency = Currency.INR,
    val groupType: Type = Type.None,
    val isActive: Boolean = true,
    val whiteBoard: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val groupID: String = UUID.randomUUID().toString(),
//    firestore items for query
    val groupMembers: List<String> = listOf(createdByUid),
//    members in group
    val members: List<GroupMember> = emptyList()
)

@Keep
data class GroupMember(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val pic: String = "",
    val addedAt: Long = System.currentTimeMillis(),
    val groupId: String = "",
)


fun User.toGroupMember(
    groupId: String,
): GroupMember {
    return GroupMember(
        uid = uid,
        name = name,
        email = email,
        pic = photoUrl,
        groupId = groupId
    )
}

@Keep
enum class SplitType {
    EQUAL, PERCENTAGE
}

/**
 * This is for global money list for group members.
 */
@Keep
data class TransactionGlobalModel(
    val path: String = "",
    val totalOwe: Double = 0.0,
    val totalAmountPaid: Double = 0.0,
    val paidByUid: String = ""
)

@Keep
data class SplitTransactionElement(
    val groupMember: GroupMember = GroupMember(),
    val amount: Double = 0.0,
    val percentage: Double? = null,
    val created: Long = System.currentTimeMillis()
)


fun SplitTransaction.toTransactionGlobalModel(
    paidByUid: String
): List<TransactionGlobalModel> =
    this.splitMembers.map { value ->
        TransactionGlobalModel(
            path = value.groupMember.uid,
            totalOwe = value.amount,
            totalAmountPaid = this.splitAmount,
            paidByUid = paidByUid
        )
    }


@Keep
data class SplitTransaction(
    val amount: Double = 0.0,
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val createdByUid: String = "",
    val splitType: SplitType = SplitType.EQUAL,
    val splitAmount: Double = 0.0,
    val splitMembers: List<SplitTransactionElement> = emptyList(),
    val isSettled: Boolean = false,
    val id: String = UUID.randomUUID().toString(),
)