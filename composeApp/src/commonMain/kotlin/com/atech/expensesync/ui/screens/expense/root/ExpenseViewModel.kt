package com.atech.expensesync.ui.screens.expense.root

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.expensesync.database.pref.PrefKeys
import com.atech.expensesync.database.pref.PrefManager
import com.atech.expensesync.database.room.expense.ExpenseBook
import com.atech.expensesync.database.room.expense.ExpenseBookEntry
import com.atech.expensesync.firebase.util.FirebaseResponse
import com.atech.expensesync.uploadData.ExpenseBookSyncUseCases
import com.atech.expensesync.usecases.ExpenseUseCases
import kotlinx.coroutines.launch
import java.util.Calendar

class ExpenseViewModel(
    private val useCase: ExpenseUseCases,
    private val expenseSyncUserCase: ExpenseBookSyncUseCases,
    private val pref: PrefManager
) : ViewModel() {
    private val _expenseBook = mutableStateOf(
        ExpenseBook(
            bookName = ""
        )
    )
    val expenseBook: State<ExpenseBook> get() = _expenseBook

    val expenseBooks = expenseSyncUserCase.expenseBookDataSyncUseCase(
        pref.getString(PrefKeys.USER_ID)
    )

    val expenseBookEntry = expenseSyncUserCase.expenseBookDataEntrySyncUseCase(
        pref.getString(PrefKeys.USER_ID)
    )

    init {
        viewModelScope.launch {
            expenseBookEntry.collect { it ->
                when (it) {
                    is FirebaseResponse.Success<List<ExpenseBookEntry>> -> {
                        com.atech.expensesync.utils.expenseSyncLogger(
                            "Data ${it.data}"
                        )
                    }

                    is FirebaseResponse.Error -> {
                        com.atech.expensesync.utils.expenseSyncLogger(
                            "Expense Sync ${it.error}"
                        )
                    }

                    else -> {}
                }
            }
        }
    }


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