package com.atech.expensesync.ui.screens.meal.view

import com.atech.expensesync.database.room.meal.MealBookEntry

sealed interface ViewMealEvents {
    data class SetMealBookId(val mealBookId: String) : ViewMealEvents
    data class SetCalendarMonth(val calendarMonth: CalendarMonthInternal) : ViewMealEvents
    data class UpdateMealBookEntry(
        val mealBookEntry: MealBookEntry,
        val oldMealBookEntry: MealBookEntry?,
        val onComplete: (Long) -> Unit = {}
    ) :
        ViewMealEvents

    data class OnDeleteMealBookEntry(val mealBookEntry: MealBookEntry) : ViewMealEvents
}