package com.atech.expensesync.ui.screens.meal.view

sealed interface ViewMealState {
    data class SetMealBookId(val mealBookId: String) : ViewMealState
    data class SetCalendarMonth(val calendarMonth: CalendarMonthInternal) : ViewMealState
}