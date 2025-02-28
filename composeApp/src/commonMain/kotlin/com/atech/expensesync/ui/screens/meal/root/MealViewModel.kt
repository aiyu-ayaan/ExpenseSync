package com.atech.expensesync.ui.screens.meal.root

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MealViewModel() : ViewModel() {
    private val _addMealState = mutableStateOf<AddMealBookState?>(null)
    val addMealState: State<AddMealBookState?> get() = _addMealState

    fun onEvent(event: MealScreenEvents) {
        when (event) {
            MealScreenEvents.OnAddMeal -> {

            }

            is MealScreenEvents.OnMealScreenStateChange ->
                _addMealState.value = event.state

        }
    }
}