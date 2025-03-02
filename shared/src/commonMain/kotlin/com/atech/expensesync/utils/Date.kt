package com.atech.expensesync.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
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