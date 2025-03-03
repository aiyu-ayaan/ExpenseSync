package com.atech.expensesync.ui.screens.meal.root

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.expensesync.usecases.MealBookUseCases
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar

class MealViewModel(
    private val useCases: MealBookUseCases
) : ViewModel() {
    private val _addMealState = mutableStateOf<AddMealBookState?>(null)
    val addMealState: State<AddMealBookState?> get() = _addMealState

    val mealBooks = useCases.getMealBooks.invoke()

    val mapper by lazy { AddMealBookStateTOMealBookMapper() }

    fun calculateTotalForCurrentMonth(
        mealBookId: String
    ) = viewModelScope.async {
        val calendar = Calendar.getInstance()
            .apply {
                set(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        val startOfMonth = calendar.timeInMillis
        calendar.add(Calendar.MONTH, 1)
        val endOfMonth = calendar.timeInMillis
        useCases.getTotalPrice
            .invoke(
                mealBookId,
                startOfMonth,
                endOfMonth
            )
    }.run {
        runBlocking { await() }
    }

    fun calculateTotalForLastMonth(
        mealBookId: String
    ) = viewModelScope.async {
        val calendar = Calendar.getInstance()
            .apply {
                set(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        calendar.add(Calendar.MONTH, -1)
        val startOfMonth = calendar.timeInMillis
        calendar.add(Calendar.MONTH, 1)
        val endOfMonth = calendar.timeInMillis
        useCases.getTotalPrice
            .invoke(
                mealBookId,
                startOfMonth,
                endOfMonth
            )
    }.run {
        runBlocking { await() }
    }

    fun onEvent(event: MealScreenEvents) {
        when (event) {
            is MealScreenEvents.OnAddMeal -> viewModelScope.launch {
                event.onComplete(
                    useCases.createMealBook.invoke(
                        mapper.mapFromEntity(
                            addMealState.value ?: return@launch
                        )
                    )
                )
            }

            is MealScreenEvents.OnMealScreenStateChange -> _addMealState.value = event.state
            is MealScreenEvents.AddMealBookEntry -> viewModelScope.launch {
                event.onComplete.invoke(
                    useCases.createMealBookEntry.invoke(event.mealBookEntry)
                )
            }

            is MealScreenEvents.DeleteMealBook -> viewModelScope.launch {
                useCases.deleteMealBook.invoke(event.mealBookId)
                event.onComplete.invoke()
            }

            is MealScreenEvents.UpdateMealBook ->
                viewModelScope.launch {
                    event.onComplete(
                        useCases.updateMealBook.invoke(
                            mapper.mapFromEntity(event.model)
                        )
                    )
                }

        }
    }
}