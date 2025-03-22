package com.atech.expensesync.ui.screens.expense.root

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.expensesync.database.room.expense.ExpenseBook
import com.atech.expensesync.usecases.ExpenseUseCases
import kotlinx.coroutines.launch

class ExpenseViewModel(
    private val useCase: ExpenseUseCases
) : ViewModel() {
    private val _expenseBook = mutableStateOf(
        ExpenseBook(
            bookName = ""
        )
    )
    val expenseBook: State<ExpenseBook> get() = _expenseBook

    val expenseBooks = useCase.getAllExpenses()


    private val _clickedExpenseBook = mutableStateOf<ExpenseBook?>(null)
    val clickedExpenseBook: State<ExpenseBook?> get() = _clickedExpenseBook

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
                _clickedExpenseBook.value = null
            }

            is ExpanseEvents.OnSaveExpense -> viewModelScope.launch {
                event.onComplete(
                    useCase.insertExpense.invoke(
                        _expenseBook.value
                    )
                )
            }

            is ExpanseEvents.OnExpenseBookClick -> {
                _clickedExpenseBook.value = event.expenseBook
            }
        }
    }

}