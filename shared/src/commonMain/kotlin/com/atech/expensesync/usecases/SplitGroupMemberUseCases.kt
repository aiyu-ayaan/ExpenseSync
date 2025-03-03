package com.atech.expensesync.usecases

import com.atech.expensesync.database.room.split.SplitGroupMemberDao
import com.atech.expensesync.database.room.split.SplitGroupMembers

class SplitGroupMemberUseCases(
    val insert: InsertMember,
    val getAll: GetGroupMembers,
    val remove: RemoveMember
)

data class InsertMember(private val dao: SplitGroupMemberDao) {
    suspend operator fun invoke(data: SplitGroupMembers) {
        dao.insertMember(data)
    }

    suspend operator fun invoke(data: List<SplitGroupMembers>) {
        dao.insertMembers(data)
    }
}

data class GetGroupMembers(private val dao: SplitGroupMemberDao) {
    operator fun invoke(groupId: String) = dao.getGroupMembers(groupId)
}

data class RemoveMember(private val dao: SplitGroupMemberDao) {
    suspend operator fun invoke(data: SplitGroupMembers) {
        dao.removeMember(data)
    }
}
