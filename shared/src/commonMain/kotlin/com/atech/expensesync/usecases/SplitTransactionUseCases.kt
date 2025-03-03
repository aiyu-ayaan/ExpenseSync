package com.atech.expensesync.usecases

import com.atech.expensesync.database.room.split.SplitTransactionDao
import com.atech.expensesync.database.room.split.SplitTransactions


data class SplitTransactionUseCases(
    val createNewTransaction: CreateNewTransactionUseCase,
    val getAllTransactions: GetTransactionsUseCase,
    val update: UpdateTransactionUseCase,
    val delete: DeleteTransactionUseCase,
    val mapTransactionWithSplitAndThenUser: MapTransactionWithSplitAndThenUser
)

data class CreateNewTransactionUseCase(
    val dao: SplitTransactionDao
) {
    suspend operator fun invoke(
        data: SplitTransactions
    ) {
        dao.insertTransactionWithSplits(data)
    }
}

data class GetTransactionsUseCase(
    val dao: SplitTransactionDao
) {
    operator fun invoke(groupId: String) = dao.getGroupTransactions(groupId)
}

data class UpdateTransactionUseCase(
    val dao: SplitTransactionDao
) {
    suspend operator fun invoke(
        data: SplitTransactions
    ) {
        dao.updateTransaction(data)
    }
}

data class DeleteTransactionUseCase(
    val dao: SplitTransactionDao
) {
    suspend operator fun invoke(
        data: SplitTransactions
    ) {
        dao.deleteTransaction(data)
    }
}


data class MapTransactionWithSplitAndThenUser(
    val dao: SplitTransactionDao
) {
    suspend operator fun invoke(
        groupId: String
    ) =
        dao.mapTransactionWithSplitAndThenUser(groupId)

}