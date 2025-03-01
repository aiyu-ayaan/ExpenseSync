package com.atech.expensesync.ui.screens.meal.root

sealed interface MealScreenEvents {
    data class OnMealScreenStateChange(val state: AddMealBookState?) : MealScreenEvents
    data class OnAddMeal(val onComplete: (Long) -> Unit) : MealScreenEvents
}