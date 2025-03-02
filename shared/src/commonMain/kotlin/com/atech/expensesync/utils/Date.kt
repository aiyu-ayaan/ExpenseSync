package com.atech.expensesync.utils

import com.atech.expensesync.database.room.meal.MealBookEntry
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

/**
 * Date pattern
 * This enum class is used to represent the date pattern
 * @property pattern The pattern
 * @constructor Create empty Date pattern
 * @param pattern The pattern
 */
enum class DatePattern(val pattern: String) {
    DD_MM_YYYY("dd-MMM yy"),
    MMM_YY("MMM yy"),
    HH_MM_A("hh:mm a"),
}

/**
 * Convert to date format
 * This function is used to convert the long to date format
 * @param format [DatePattern]
 * @return [String]
 */
fun Long.convertToDateFormat(format: DatePattern = DatePattern.DD_MM_YYYY): String = this.run {
    val instant = Instant.fromEpochMilliseconds(this)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val formatter = DateTimeFormatter.ofPattern(format.pattern)
    localDateTime.toJavaLocalDateTime().format(formatter)
}

fun checkIts1stDayOfMonth(date: Long = System.currentTimeMillis()): Boolean {
    val instant = Instant.fromEpochMilliseconds(date)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return localDateTime.dayOfMonth == 1
}

fun checkItsLastDayOfMonth(date: Long = System.currentTimeMillis()): Boolean {
    val instant = Instant.fromEpochMilliseconds(date)
    val localDateTime: LocalDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return localDateTime.dayOfMonth == localDateTime.month.length(localDateTime.isLeapYear())
}

/**
 * Get current day and total days of the month
 */
fun getCurrentDayAndTotalDays(): Pair<Int, Int> {
    val instant = Instant.fromEpochMilliseconds(System.currentTimeMillis())
    val localDateTime: LocalDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return Pair(localDateTime.dayOfMonth, localDateTime.month.length(localDateTime.isLeapYear()))
}

private fun LocalDateTime.isLeapYear(): Boolean {
    return this.year % 4 == 0 && (this.year % 100 != 0 || this.year % 400 == 0)
}

infix fun Long.isSameMonth(other: Long): Boolean {
    val calendar1 = Calendar.getInstance().apply { timeInMillis = this@isSameMonth }
    val calendar2 = Calendar.getInstance().apply { timeInMillis = other }

    return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
            calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
}

infix fun Long.isSameDay(other: Long): Boolean {
    val calendar1 = Calendar.getInstance().apply { timeInMillis = this@isSameDay }
    val calendar2 = Calendar.getInstance().apply { timeInMillis = other }

    return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
            calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
}

fun List<MealBookEntry>.generatePriceSumOfBasicOfWeek(
    present: Month
): Pair<List<Double>, List<Double>> {
    // Get current date info for proper year calculation
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val currentYear = now.year

    // Define previous month
    val previousMonth = if (present.number == 1) Month.DECEMBER else Month.of(present.number - 1)
    val previousMonthYear = if (present.number == 1) currentYear - 1 else currentYear

    // Convert entries to LocalDate for easier date manipulation
    val entriesWithDate = this.map { entry ->
        val instant = Instant.fromEpochMilliseconds(entry.createdAt)
        val date = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        Pair(entry, date)
    }

    // Filter entries for current and previous month
    val currentMonthEntries = entriesWithDate.filter { (_, date) ->
        date.month == present && date.year == currentYear
    }
    val previousMonthEntries = entriesWithDate.filter { (_, date) ->
        date.month == previousMonth && date.year == previousMonthYear
    }

    // Calculate price sums by week for current month (support up to 5 weeks)
    val currentMonthSums = MutableList(5) { 0.0 }
    currentMonthEntries.forEach { (entry, date) ->
        val weekOfMonth = getWeekOfMonth(date) - 1 // Convert to 0-based index
        if (weekOfMonth in 0..4) {
            currentMonthSums[weekOfMonth] += entry.price
        }
    }

    // Calculate price sums by week for previous month (support up to 5 weeks)
    val previousMonthSums = MutableList(5) { 0.0 }
    previousMonthEntries.forEach { (entry, date) ->
        val weekOfMonth = getWeekOfMonth(date) - 1 // Convert to 0-based index
        if (weekOfMonth in 0..4) {
            previousMonthSums[weekOfMonth] += entry.price
        }
    }

    return Pair(previousMonthSums, currentMonthSums)
}

// Helper function to calculate week of month (1-based)
private fun getWeekOfMonth(date: LocalDate): Int {
    val firstDayOfMonth = LocalDate(date.year, date.month, 1)
    val daysSinceStartOfMonth = date.dayOfMonth - 1
    val adjustedDayOfWeek = (firstDayOfMonth.dayOfWeek.ordinal + daysSinceStartOfMonth) % 7
    return (daysSinceStartOfMonth + adjustedDayOfWeek) / 7 + 1
}