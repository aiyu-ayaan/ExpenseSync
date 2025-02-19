package com.atech.expensesync.usecases

import com.atech.expensesync.database.room.split.ExpanseGroupMemberDao
import com.atech.expensesync.database.room.split.ExpenseGroupMembers

class ExpanseGroupMemberUseCases(
    val insert: InsertMember,
    val getAll: GetGroupMembers,
    val remove: RemoveMember
)

data class InsertMember(private val dao: ExpanseGroupMemberDao) {
    suspend operator fun invoke(data: ExpenseGroupMembers) {
        dao.insertMember(data)
    }

    suspend operator fun invoke(data: List<ExpenseGroupMembers>) {
        dao.insertMembers(data)
    }
}

data class GetGroupMembers(private val dao: ExpanseGroupMemberDao) {
    operator fun invoke(groupId: String) = dao.getGroupMembers(groupId)
}

data class RemoveMember(private val dao: ExpanseGroupMemberDao) {
    suspend operator fun invoke(data: ExpenseGroupMembers) {
        dao.removeMember(data)
    }
}
