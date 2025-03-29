package com.atech.expensesync.utils

import com.atech.expensesync.database.room.meal.MealBookEntry
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
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
import java.util.Locale

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
    HH_MM_12("hh:mm a"),
    DD_MM_YYYY_HH_MM_A("dd-MMM yy hh:mm a"),
    DD_MM_YYYY_2("dd/MM/yyyy"),
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

    // Get first day of week from locale
    val firstDayOfWeek = firstDayOfWeek()

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
        val weekOfMonth = getWeekOfMonth(date, firstDayOfWeek) - 1 // Convert to 0-based index
        if (weekOfMonth in 0..4) {
            currentMonthSums[weekOfMonth] += entry.price
        }
    }

    // Calculate price sums by week for previous month (support up to 5 weeks)
    val previousMonthSums = MutableList(5) { 0.0 }
    previousMonthEntries.forEach { (entry, date) ->
        val weekOfMonth = getWeekOfMonth(date, firstDayOfWeek) - 1 // Convert to 0-based index
        if (weekOfMonth in 0..4) {
            previousMonthSums[weekOfMonth] += entry.price
        }
    }

    return Pair(previousMonthSums, currentMonthSums)
}

// Helper function to calculate week of month (1-based) based on locale's first day of week
private fun getWeekOfMonth(date: LocalDate, firstDayOfWeek: DayOfWeek): Int {
    val firstDayOfMonth = LocalDate(date.year, date.month, 1)

    // Calculate days to adjust based on first day of week from locale
    val daysToAdjust = (firstDayOfMonth.dayOfWeek.ordinal - firstDayOfWeek.ordinal + 7) % 7

    // Calculate adjusted day position
    val daysSinceStartOfMonth = date.dayOfMonth - 1
    val adjustedDayPosition = daysSinceStartOfMonth + daysToAdjust

    return adjustedDayPosition / 7 + 1
}

/**
 * Gets the first day of week based on the current locale using Kotlin's datetime library.
 * @return The first day of week as a kotlinx.datetime.DayOfWeek enum
 */
fun firstDayOfWeek(): DayOfWeek {
    val calendar = Calendar.getInstance(Locale.getDefault())
    val firstDayOfWeek = calendar.firstDayOfWeek

    // Convert from Calendar.MONDAY (value 2) to kotlinx.datetime.DayOfWeek.MONDAY (value 1)
    return when (firstDayOfWeek) {
        Calendar.SUNDAY -> DayOfWeek.SUNDAY
        Calendar.MONDAY -> DayOfWeek.MONDAY
        Calendar.TUESDAY -> DayOfWeek.TUESDAY
        Calendar.WEDNESDAY -> DayOfWeek.WEDNESDAY
        Calendar.THURSDAY -> DayOfWeek.THURSDAY
        Calendar.FRIDAY -> DayOfWeek.FRIDAY
        Calendar.SATURDAY -> DayOfWeek.SATURDAY
        else -> DayOfWeek.MONDAY // Default to Monday if something unexpected happens
    }
}