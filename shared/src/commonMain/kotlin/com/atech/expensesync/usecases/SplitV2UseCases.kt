package com.atech.expensesync.usecases

import com.atech.expensesync.database.room.splitv2.GroupMembers
import com.atech.expensesync.database.room.splitv2.SplitDao
import com.atech.expensesync.database.room.splitv2.SplitGlobalTransactions
import com.atech.expensesync.database.room.splitv2.SplitModel
import com.atech.expensesync.database.room.splitv2.SplitTransactions
import com.atech.expensesync.database.room.splitv2.SplitType
import com.atech.expensesync.utils.Currency
import kotlinx.coroutines.Dispatchers


data class SplitV2UseCases(
    val createSplitGroup: CreateSplitGroup,
    val updateSplitGroup: UpdateSplitGroup,
    val getSplitGroups: GetSplitGroups,
    val updateSplitGroupMember: UpdateSplitGroupMember,
    val getAllGlobalTransactions: GetAllGlobalTransactions,
    val getSplitTransactions: GetSplitTransactions,
    val createSplitTransaction: CreateSplitTransaction,
    val getSplitGroupMembers: GetSplitGroupMembers,
    val insertSplitGroupMember: InsertSplitGroupMember,
)

data class CreateSplitGroup(
    private val splitDao: SplitDao
) {
    suspend operator fun invoke(
        splitModel: SplitModel,
        groupMembers: List<GroupMembers>
    ) = splitDao.createSplitGroup(
        splitModel,
        groupMembers
    )

    suspend operator fun invoke(
        splitModel: SplitModel,
        groupMembers: GroupMembers
    ) = splitDao.createSplitGroup(
        splitModel,
        listOf(groupMembers)
    )
}

data class UpdateSplitGroup(
    private val splitDao: SplitDao
) {
    suspend operator fun invoke(
        splitModel: SplitModel
    ) = splitDao.updateSplitGroup(
        splitModel
    )
}

data class GetSplitGroups(
    private val splitDao: SplitDao
) {
    operator fun invoke() = splitDao.getSplitGroups()
}

data class UpdateSplitGroupMember(
    private val splitDao: SplitDao
) {
    suspend operator fun invoke(
        groupMembers: GroupMembers
    ) = splitDao.updateSplitGroupMember(
        groupMembers
    )
}


data class GetAllGlobalTransactions(
    private val splitDao: SplitDao
) {
    operator fun invoke(
        groupId: String
    ) = splitDao.getSplitGlobalTransaction(groupId)
}


data class GetSplitTransactions(
    private val splitDao: SplitDao
) {
    operator fun invoke(
        groupId: String
    ) = splitDao.getSplitTransactions(groupId)
}

data class CreateSplitTransaction(
    private val splitDao: SplitDao
) {
    suspend operator fun invoke(
        groupId: String,
        amount: Double,
        paidByUid: String,
        description: String = "",
        createdAt: Long = System.currentTimeMillis(),
        currency: Currency = Currency.INR,
        splitType: SplitType = SplitType.EQUAL,
    ) = with(Dispatchers.IO) {
        val members = splitDao.getSplitGroupMembersInternal(groupId)
        val size = members.size
        members.forEach {
            val splitGlobalTransaction = splitDao.getMemberSplitGlobalTransaction(
                groupId,
                it.uid
            )
            if (splitGlobalTransaction != null)
                splitDao.updateSplitGlobalTransactions(
                    splitGlobalTransaction.copy(
                        totalAmountOwe = splitGlobalTransaction.totalAmountOwe + (amount / size),
                    )
                )
            else
                splitDao.insertSplitGlobalTransactions(
                    SplitGlobalTransactions(
                        groupId = groupId,
                        groupMemberUid = it.uid,
                        totalAmountOwe = amount / size,
                        currency = currency,
                    )
                )

            splitDao.insertSplitTransaction(
                SplitTransactions(
                    groupId = groupId,
                    amount = amount / size,
                    description = description,
                    paidByUid = paidByUid,
                    splitType = splitType,
                    createdAt = createdAt,
                )
            )
        }
    }
}

data class GetSplitGroupMembers(
    private val splitDao: SplitDao
) {
    suspend operator fun invoke(
        groupId: String
    ) = splitDao.getSplitGroupMembers(groupId)
}

data class InsertSplitGroupMember(
    private val splitDao: SplitDao
) {
    suspend operator fun invoke(
        groupMembers: GroupMembers
    ) = splitDao.insertSplitGroupMember(
        groupMembers
    )

    suspend operator fun invoke(
        groupMembers: List<GroupMembers>
    ) = splitDao.insertSplitGroupMembers(
        groupMembers
    )
}