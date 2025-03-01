package com.atech.expensesync.ui.screens.meal.root

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.expensesync.usecases.MealBookUseCases
import kotlinx.coroutines.launch

class MealViewModel(
    private val useCases: MealBookUseCases
) : ViewModel() {
    private val _addMealState = mutableStateOf<AddMealBookState?>(null)
    val addMealState: State<AddMealBookState?> get() = _addMealState

    private val mapper = AddMealBookStateTOMealBookMapper()

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
        }
    }
}