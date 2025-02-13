package com.atech.expensesync.ui.screens.split.add

import com.atech.expensesync.database.models.User
import com.atech.expensesync.database.room.split.ExpanseGroupMembers
import com.atech.expensesync.database.room.split.ExpanseTransactions
import com.atech.expensesync.database.room.split.SplitType
import com.atech.expensesync.utils.EntityMapper
import com.atech.expensesync.utils.convertToDateFormat

data class ViewExpenseBookState(
    val groupName: String = "",
    val groupId: String = "",
)


data class CreateExpenseState(
    val description: String,
    val amount: Double,
    val paidBy: ExpanseGroupMembers,
    val splitType: SplitType = SplitType.EQUAL,
    val splitTo: List<ExpanseGroupMembers> = emptyList(),
    val date: Long = System.currentTimeMillis()
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
            paidBy = ExpanseGroupMembers(
                uid = userModel.uid,
                name = userModel.name,
                email = userModel.email,
                pic = userModel.photoUrl,
                groupId = groupId
            ),
            splitType = SplitType.EQUAL,
            splitTo = emptyList()
        )
    }
}

class CreateExpenseStateToExpanseTransactionsMapper :
    EntityMapper<CreateExpenseState?, ExpanseTransactions> {
    override fun mapFromEntity(entity: CreateExpenseState?): ExpanseTransactions =
        ExpanseTransactions(
            groupId = entity!!.paidBy.groupId,
            amount = entity.amount,
            description = entity.description,
            paidBy = entity.paidBy.key,
            splitType = entity.splitType,
        )


    override fun mapToEntity(domainModel: ExpanseTransactions): CreateExpenseState? = null

}