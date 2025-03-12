package com.atech.expensesync.usecases

import com.atech.expensesync.database.room.expense.ExpenseBook
import com.atech.expensesync.database.room.expense.ExpenseBookDao
import com.atech.expensesync.database.room.expense.ExpenseBookEntry


data class ExpenseUseCases(
    val insertExpense: InsertExpenseUseCase,
    val insertExpenseEntry: InsertExpenseEntryUseCase,
    val updateExpense: UpdateExpenseUseCase,
    val updateExpenseEntry: UpdateExpenseEntryUseCase,
    val deleteExpense: DeleteExpenseUseCase,
    val deleteExpenseEntry: DeleteExpenseEntryUseCase,
    val getAllExpenses: GetAllExpensesUseCase,
    val getExpenseById: GetExpenseByIdUseCase,
    val getExpenseBookEntry: GetExpenseBookEntryUseCase,
    val updateTotalAmount: UpdateTotalAmountUseCase,
    val updateTotalIn: UpdateTotalInUseCase,
    val updateTotalOut: UpdateTotalOutUseCase,
    val addTransaction: AddTransactionUseCase
)

data class InsertExpenseUseCase(
    private val dao: ExpenseBookDao
) {
    suspend operator fun invoke(
        expense: ExpenseBook
    ) = dao.insertExpense(expense)
}

data class InsertExpenseEntryUseCase(
    private val dao: ExpenseBookDao
) {
    suspend operator fun invoke(
        expenses: ExpenseBookEntry
    ) = dao.insertExpenseEntry(expenses)
}


data class UpdateExpenseUseCase(
    private val dao: ExpenseBookDao
) {
    suspend operator fun invoke(
        expense: ExpenseBook
    ) = dao.updateExpense(expense)
}

data class UpdateExpenseEntryUseCase(
    private val dao: ExpenseBookDao
) {
    suspend operator fun invoke(
        expenses: ExpenseBookEntry
    ) = dao.updateExpenseEntry(expenses)
}

data class DeleteExpenseUseCase(
    private val dao: ExpenseBookDao
) {
    suspend operator fun invoke(
        expense: ExpenseBook
    ) = dao.deleteExpense(expense)
}

data class DeleteExpenseEntryUseCase(
    private val dao: ExpenseBookDao
) {
    suspend operator fun invoke(
        expenses: ExpenseBookEntry
    ) = dao.deleteExpenseEntry(expenses)
}


data class GetAllExpensesUseCase(
    private val dao: ExpenseBookDao
) {
    operator fun invoke() = dao.getAllExpenses()
}

data class GetExpenseByIdUseCase(
    private val dao: ExpenseBookDao
) {
    operator fun invoke(
        bookId: String
    ) = dao.getExpenseById(bookId)
}

data class GetExpenseBookEntryUseCase(
    private val dao: ExpenseBookDao
) {
    operator fun invoke(
        bookId: String
    ) = dao.getExpenseBookEntry(bookId)
}

data class UpdateTotalAmountUseCase(
    private val dao: ExpenseBookDao
) {
    suspend operator fun invoke(
        bookId: String,
        amount: Double
    ) = dao.updateTotalAmount(bookId, amount)
}

data class UpdateTotalInUseCase(
    private val dao: ExpenseBookDao
) {
    suspend operator fun invoke(
        bookId: String,
        amount: Double
    ) = dao.updateTotalIn(bookId, amount)
}

data class UpdateTotalOutUseCase(
    private val dao: ExpenseBookDao
) {
    suspend operator fun invoke(
        bookId: String,
        amount: Double
    ) = dao.updateTotalOut(bookId, amount)
}

data class AddTransactionUseCase(
    private val dao: ExpenseBookDao
) {
    suspend operator fun invoke(
        expenseBookEntry: ExpenseBookEntry
    ) = dao.addTransaction(expenseBookEntry)
}