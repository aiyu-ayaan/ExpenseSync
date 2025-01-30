package com.atech.expensesync.usecases

import com.atech.expensesync.database.room.split.SplitDao
import com.atech.expensesync.database.room.split.SplitGroup


data class SplitUseCases(
    val createNewGroup: CreateNewGroupUseCase,
    val getAllGroups: GetGroupsUseCase,
    val update: UpdateGroupUseCase,
    val delete: DeleteGroupUseCase,
)

data class CreateNewGroupUseCase(
    val dao: SplitDao
) {
    suspend operator fun invoke(
        data: SplitGroup
    ) {
        dao.insert(data)
    }
}

data class GetGroupsUseCase(
    val dao: SplitDao
) {
    operator fun invoke() =
        dao.getAll()
}

data class UpdateGroupUseCase(
    val dao: SplitDao
) {
    suspend operator fun invoke(
        data: SplitGroup
    ) {
        dao.update(data)
    }
}

data class DeleteGroupUseCase(
    val dao: SplitDao
) {
    suspend operator fun invoke(
        data: SplitGroup
    ) {
        dao.delete(data)
    }
}

