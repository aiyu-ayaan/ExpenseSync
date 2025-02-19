package com.atech.expensesync.ui.screens.split.add

import com.atech.expensesync.database.models.User
import com.atech.expensesync.database.room.split.ExpenseGroupMembers
import com.atech.expensesync.database.room.split.ExpenseTransactions
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
    val paidBy: ExpenseGroupMembers,
    val splitType: SplitType = SplitType.EQUAL,
    val splitTo: List<ExpenseGroupMembers> = emptyList(),
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
            paidBy = ExpenseGroupMembers(
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
    EntityMapper<CreateExpenseState?, ExpenseTransactions> {
    override fun mapFromEntity(entity: CreateExpenseState?): ExpenseTransactions =
        ExpenseTransactions(
            groupId = entity!!.paidBy.groupId,
            amount = entity.amount,
            description = entity.description,
            paidByKey = entity.paidBy.key,
            splitType = entity.splitType,
        )


    override fun mapToEntity(domainModel: ExpenseTransactions): CreateExpenseState? = null

}