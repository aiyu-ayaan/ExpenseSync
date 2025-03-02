package com.atech.expensesync.ui.screens.meal.view

import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.YearMonth

data class CalendarMonthInternal(
    val yearMonth: YearMonth,
    val weekDays: List<List<CalendarDay>>,
)

fun CalendarMonth.toInternal(): CalendarMonthInternal {
    return CalendarMonthInternal(
        yearMonth = yearMonth,
        weekDays = weekDays
    )
}