package com.atech.expensesync.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter

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

fun checkIts1stDayOfMonth(date: Long): Boolean {
    val instant = Instant.fromEpochMilliseconds(date)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return localDateTime.dayOfMonth == 1
}

fun checkItsLastDayOfMonth(date: Long): Boolean {
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