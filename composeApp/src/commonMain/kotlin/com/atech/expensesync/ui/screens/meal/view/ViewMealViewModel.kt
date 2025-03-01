package com.atech.expensesync.ui.screens.meal.view

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.expensesync.database.room.meal.MealBookEntry
import com.atech.expensesync.usecases.MealBookUseCases
import com.kizitonwose.calendar.core.YearMonth
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ViewMealViewModel(
    private val useCase: MealBookUseCases
) : ViewModel() {

    private var _mealBookId: String? = null
    private val _mealBookEntryState = mutableStateOf<List<MealBookEntry>>(emptyList())
    val mealBookEntryState: State<List<MealBookEntry>> get() = _mealBookEntryState

    private val _calenderMonth = mutableStateOf(
        CalendarMonthInternal(
            yearMonth = YearMonth.now(),
            weekDays = emptyList()
        )
    )
    val calenderMonth: State<CalendarMonthInternal> get() = _calenderMonth

    internal fun onEvent(
        onEvent: ViewMealState
    ) {
        when (onEvent) {
            is ViewMealState.SetMealBookId -> {
                if (_mealBookId != null) return
                _mealBookId = onEvent.mealBookId
                loadData()
            }

            is ViewMealState.SetCalendarMonth -> {
                _calenderMonth.value = onEvent.calendarMonth
            }
        }
    }

    private fun loadData() {
        useCase.getMealBookEntries(_mealBookId ?: return)
            .onEach {
                _mealBookEntryState.value = it
            }.launchIn(viewModelScope)
    }
}