package com.atech.expensesync.ui.screens.split.add

import com.atech.expensesync.database.models.User
import com.atech.expensesync.database.room.split.SplitGroupMembers
import com.atech.expensesync.database.room.split.SplitTransactions
import com.atech.expensesync.database.room.split.SplitType
import com.atech.expensesync.utils.EntityMapper
import com.atech.expensesync.utils.convertToDateFormat
import java.util.UUID

data class ViewExpenseBookState(
    val groupName: String = "",
    val groupId: String = "",
)


data class CreateExpenseState(
    val description: String,
    val amount: Double,
    val paidBy: SplitGroupMembers,
    val splitType: SplitType = SplitType.EQUAL,
    val splitTo: List<SplitGroupMembers> = emptyList(),
    val date: Long = System.currentTimeMillis(),
    val transactionId: String,
) {
    val formatedDate: String
        get() = date.convertToDateFormat()

    companion object {
        fun default(
            userModel: User,
            groupId: String = "",
        ) = CreateExpenseState(
            description = "",
            amount = 0.0,
            paidBy = SplitGroupMembers(
                uid = userModel.uid,
                name = userModel.name,
                email = userModel.email,
                pic = userModel.photoUrl,
                groupId = groupId
            ),
            splitType = SplitType.EQUAL,
            splitTo = emptyList(),
            transactionId = ""
        )
    }
}

class CreateExpenseStateToExpanseTransactionsMapper :
    EntityMapper<CreateExpenseState?, SplitTransactions> {
    override fun mapFromEntity(entity: CreateExpenseState?): SplitTransactions =
        SplitTransactions(
            groupId = entity!!.paidBy.groupId,
            amount = entity.amount,
            description = entity.description,
            paidByKey = entity.paidBy.key,
            splitType = entity.splitType,
            transactionId = entity.transactionId.ifEmpty {
                UUID.randomUUID()
                    .toString()
            },
            createdAt = entity.date
        )


    override fun mapToEntity(domainModel: SplitTransactions): CreateExpenseState? = null

    fun mapToCreateExpenseState(
        domainModel: SplitTransactions,
        splitTo: List<SplitGroupMembers>
    ): CreateExpenseState =
        CreateExpenseState(
            description = domainModel.description,
            amount = domainModel.amount,
            paidBy = splitTo.first { it.key == domainModel.paidByKey },
            splitType = domainModel.splitType,
            splitTo = splitTo,
            transactionId = domainModel.transactionId,
            date = domainModel.createdAt,
        )
}