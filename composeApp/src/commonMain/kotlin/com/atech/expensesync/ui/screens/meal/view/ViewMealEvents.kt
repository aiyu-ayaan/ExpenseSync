package com.atech.expensesync.ui.screens.meal.view

import com.atech.expensesync.database.room.meal.MealBookEntry
import com.atech.expensesync.ui.screens.meal.root.AddMealBookState

sealed interface ViewMealEvents {
    data class SetMealBookId(
        val mealBookId: String,
        val state: AddMealBookState
    ) : ViewMealEvents

    data class SetCalendarMonth(val calendarMonth: CalendarMonthInternal) : ViewMealEvents
    data class UpdateMealBookEntry(
        val mealBookEntry: MealBookEntry,
        val oldMealBookEntry: MealBookEntry?,
        val onComplete: (Long) -> Unit = {}
    ) :
        ViewMealEvents

    data class OnDeleteMealBookEntry(val mealBookEntry: MealBookEntry) : ViewMealEvents
    data class OnDeleteMealBook(
        val onComplete: () -> Unit
    ) : ViewMealEvents

    data class UpdateMealBook(
        val model: AddMealBookState,
        val onComplete: (Long) -> Unit
    ) : ViewMealEvents
}