package com.atech.expensesync.ui.screens.expense.root

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.expensesync.database.room.expense.ExpenseBook
import com.atech.expensesync.database.room.expense.ExpenseBookEntry
import com.atech.expensesync.usecases.ExpenseUseCases
import kotlinx.coroutines.launch
import java.util.Calendar

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

    private val _getExpenseBookEntry =
        mutableStateOf<Map<Long, List<ExpenseBookEntry>>>(emptyMap())
    val getExpenseBookEntry: State<Map<Long, List<ExpenseBookEntry>>> get() = _getExpenseBookEntry

    fun onEvent(
        event: ExpenseEvents
    ) {
        when (event) {
            is ExpenseEvents.OnExpenseBookChange -> {
                _expenseBook.value = event.expenseBook
            }

            ExpenseEvents.ResetStates -> {
                _expenseBook.value = ExpenseBook(
                    bookName = ""
                )
                _clickedExpenseBook.value = null
            }

            is ExpenseEvents.OnSaveExpense -> viewModelScope.launch {
                event.onComplete(
                    useCase.insertExpense.invoke(
                        _expenseBook.value
                    )
                )
            }

            is ExpenseEvents.OnExpenseBookClick -> viewModelScope.launch {
                useCase.getExpenseById(
                    event.expenseBook?.bookId ?: return@launch
                ).collect {
                    _clickedExpenseBook.value = it
                }
            }

            is ExpenseEvents.OnSaveExpenseBookEntry -> {
                viewModelScope.launch {
                    useCase.addTransaction.invoke(
                        event.expenseBookEntry
                    )
                    event.onComplete()
                }
            }

            ExpenseEvents.LoadExpenseBookEntry -> viewModelScope.launch {
                useCase.getExpenseBookEntry(
                    _clickedExpenseBook.value?.bookId ?: return@launch
                ).collect {
                    _getExpenseBookEntry.value = it.groupBy { expense ->
                        val calendar = Calendar.getInstance()
                        calendar.timeInMillis = expense.createdAt
                        calendar.set(Calendar.HOUR_OF_DAY, 0)
                        calendar.set(Calendar.MINUTE, 0)
                        calendar.set(Calendar.SECOND, 0)
                        calendar.set(Calendar.MILLISECOND, 0)
                        calendar.timeInMillis
                    }
                }
            }
        }
    }

}