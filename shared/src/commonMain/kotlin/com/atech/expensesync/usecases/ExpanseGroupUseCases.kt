package com.atech.expensesync.usecases

import com.atech.expensesync.database.room.split.ExpanseGroup
import com.atech.expensesync.database.room.split.ExpanseGroupDao
import com.atech.expensesync.database.room.split.ExpanseGroupMembers


data class SplitUseCases(
    val createNewGroup: CreateNewGroupUseCase,
    val getAllGroups: GetGroupsUseCase,
    val update: UpdateGroupUseCase,
    val delete: DeleteGroupUseCase,
)

data class CreateNewGroupUseCase(
    val dao: ExpanseGroupDao
) {
    suspend operator fun invoke(
        data: ExpanseGroup,
        members: List<ExpanseGroupMembers>
    ) {
        dao.insertGroupWithMembers(
            groupName = data,
            members = members
        )
    }
}

data class GetGroupsUseCase(
    val dao: ExpanseGroupDao
) {
    operator fun invoke() = dao.getAllActiveGroups()
}

data class UpdateGroupUseCase(
    val dao: ExpanseGroupDao
) {
    suspend operator fun invoke(
        data: ExpanseGroup
    ) {
        dao.updateGroup(data)
    }
}

data class DeleteGroupUseCase(
    val dao: ExpanseGroupDao
) {
    suspend operator fun invoke(
        data: ExpanseGroup
    ) {
        dao.deleteGroup(data)
    }
}

