package com.atech.expensesync.ui.screens.expense.root

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.atech.expensesync.database.room.expense.ExpenseBook
import com.atech.expensesync.usecases.ExpenseUseCases

class ExpenseViewModel(
    private val useCase: ExpenseUseCases
) : ViewModel() {
    private val _expenseBook = mutableStateOf(
        ExpenseBook(
            bookName = ""
        )
    )
    val expenseBook: State<ExpenseBook> get() = _expenseBook

    fun onEvent(
        event: ExpanseEvents
    ) {
        when (event) {
            is ExpanseEvents.OnExpenseBookChange -> {
                _expenseBook.value = event.expenseBook
            }

            ExpanseEvents.ResetStates -> {
                _expenseBook.value = ExpenseBook(
                    bookName = ""
                )
            }
        }
    }

}