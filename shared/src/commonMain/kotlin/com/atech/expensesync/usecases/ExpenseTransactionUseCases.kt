package com.atech.expensesync.usecases

import com.atech.expensesync.database.room.split.ExpanseTransactionDao
import com.atech.expensesync.database.room.split.ExpanseTransactions


data class ExpenseTransactionUseCases(
    val createNewTransaction: CreateNewTransactionUseCase,
    val getAllTransactions: GetTransactionsUseCase,
    val update: UpdateTransactionUseCase,
    val delete: DeleteTransactionUseCase,
)

data class CreateNewTransactionUseCase(
    val dao: ExpanseTransactionDao
) {
    suspend operator fun invoke(
        data: ExpanseTransactions
    ) {
        dao.insertTransactionWithSplits(data)
    }
}

data class GetTransactionsUseCase(
    val dao: ExpanseTransactionDao
) {
    operator fun invoke(groupId: String) = dao.getGroupTransactions(groupId)
}

data class UpdateTransactionUseCase(
    val dao: ExpanseTransactionDao
) {
    suspend operator fun invoke(
        data: ExpanseTransactions
    ) {
        dao.updateTransaction(data)
    }
}

data class DeleteTransactionUseCase(
    val dao: ExpanseTransactionDao
) {
    suspend operator fun invoke(
        data: ExpanseTransactions
    ) {
        dao.deleteTransaction(data)
    }
}
