package com.atech.expensesync.ui.screens.meal.root

import com.atech.expensesync.database.room.meal.MealBookEntry

sealed interface MealScreenEvents {
    data class OnMealScreenStateChange(val state: AddMealBookState?) : MealScreenEvents
    data class OnAddMeal(val onComplete: (Long) -> Unit) : MealScreenEvents
    data class AddMealBookEntry(
        val mealBookEntry: MealBookEntry,
        val onComplete: (Long) -> Unit
    ) :
        MealScreenEvents

    data class SetMealBookEntry(
        val mealBookEntry: AddMealBookState?,
    ) : MealScreenEvents

    data class UpdateMealBook(
        val model: AddMealBookState,
        val onComplete: (Long) -> Unit
    ) : MealScreenEvents

    data class DeleteMealBook(
        val mealBookId: String,
        val onComplete: () -> Unit
    ) : MealScreenEvents
}