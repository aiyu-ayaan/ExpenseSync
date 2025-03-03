package com.atech.expensesync.usecases

import com.atech.expensesync.database.room.split.SplitGroup
import com.atech.expensesync.database.room.split.SplitGroupDao
import com.atech.expensesync.database.room.split.SplitGroupMembers


data class SplitUseCases(
    val createNewGroup: CreateNewGroupUseCase,
    val getAllGroups: GetGroupsUseCase,
    val update: UpdateGroupUseCase,
    val delete: DeleteGroupUseCase,
)

data class CreateNewGroupUseCase(
    val dao: SplitGroupDao
) {
    suspend operator fun invoke(
        data: SplitGroup,
        members: List<SplitGroupMembers>
    ) {
        dao.insertGroupWithMembers(
            groupName = data,
            members = members
        )
    }
}

data class GetGroupsUseCase(
    val dao: SplitGroupDao
) {
    operator fun invoke() = dao.getAllActiveGroups()
}

data class UpdateGroupUseCase(
    val dao: SplitGroupDao
) {
    suspend operator fun invoke(
        data: SplitGroup
    ) {
        dao.updateGroup(data)
    }
}

data class DeleteGroupUseCase(
    val dao: SplitGroupDao
) {
    suspend operator fun invoke(
        data: SplitGroup
    ) {
        dao.deleteGroup(data)
    }
}

