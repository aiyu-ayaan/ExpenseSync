package com.atech.expensesync.ui.screens.meal.view

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.expensesync.database.room.meal.MealBookEntry
import com.atech.expensesync.ui.screens.meal.root.AddMealBookState
import com.atech.expensesync.ui.screens.meal.root.AddMealBookStateTOMealBookMapper
import com.atech.expensesync.usecases.MealBookUseCases
import com.kizitonwose.calendar.core.YearMonth
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ViewMealViewModel(
    private val useCase: MealBookUseCases
) : ViewModel() {

    private var _mealBookId: String? = null
    private val _mealBookEntryState = mutableStateOf<List<MealBookEntry>>(emptyList())
    val mealBookEntryState: State<List<MealBookEntry>> get() = _mealBookEntryState

    private val _mealBookState = mutableStateOf<AddMealBookState?>(null)
    val mealBookState: State<AddMealBookState?> get() = _mealBookState

    private val _calenderMonth = mutableStateOf(
        CalendarMonthInternal(
            yearMonth = YearMonth.now(), weekDays = emptyList()
        )
    )
    val calenderMonth: State<CalendarMonthInternal> get() = _calenderMonth

    private val mapper by lazy { AddMealBookStateTOMealBookMapper() }

    internal fun onEvent(
        onEvent: ViewMealEvents
    ) {
        when (onEvent) {
            is ViewMealEvents.SetMealBookId -> {
                _mealBookId = onEvent.mealBookId
                _mealBookState.value = onEvent.state
                loadData()
            }

            is ViewMealEvents.SetCalendarMonth -> {
                _calenderMonth.value = onEvent.calendarMonth
            }

            is ViewMealEvents.UpdateMealBookEntry -> viewModelScope.launch {
                onEvent.onComplete.invoke(
                    useCase.updateMealBookEntry(
                        mealBookEntry = onEvent.mealBookEntry,
                        oldMealBookEntry = onEvent.oldMealBookEntry
                    )
                )
            }

            is ViewMealEvents.OnDeleteMealBookEntry -> viewModelScope.launch {
                useCase.deleteMealBookEntry(onEvent.mealBookEntry)
            }

            is ViewMealEvents.OnDeleteMealBook -> viewModelScope.launch {
                useCase.deleteMealBook(_mealBookId ?: return@launch)
                onEvent.onComplete.invoke()
            }

            is ViewMealEvents.UpdateMealBook -> {
                viewModelScope.launch {
                    _mealBookState.value = onEvent.model
                    onEvent.onComplete.invoke(
                        useCase.updateMealBook(
                            mapper.mapFromEntity(onEvent.model)
                        )
                    )
                }
            }
        }
    }

    private fun loadData() {
        useCase.getMealBookEntries(_mealBookId ?: return).onEach {
            _mealBookEntryState.value = it
        }.launchIn(viewModelScope)
    }
}