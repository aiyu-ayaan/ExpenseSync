package com.atech.expensesync.ui.screens.meal.view.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.room.meal.MealBookEntry
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.DeviceType
import com.atech.expensesync.ui_utils.getDisplayType
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.YearMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.minusMonths
import com.kizitonwose.calendar.core.now
import com.kizitonwose.calendar.core.plusMonths
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewMealScreen(
    modifier: Modifier = Modifier,
    mealBookName: String,
    mealBookId: String = "",
    state: List<MealBookEntry> = generateMealBookEntries(),
    onNavigateUp: () -> Unit = {}
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }
    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstDayOfWeek = firstDayOfWeek,
        firstVisibleMonth = currentMonth
    )

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    MainContainer(
        modifier = modifier,
        title = mealBookName,
        onNavigationClick = onNavigateUp
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
                .padding(MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val calenderModifier = when (getDisplayType()) {
                DeviceType.MOBILE -> Modifier.fillMaxWidth()

                else -> Modifier.fillMaxWidth(
                    .5f
                )
            }
            HorizontalCalendar(
                modifier = calenderModifier,
                state = calendarState,
                dayContent = { day ->
                    Day(
                        day = day,
                        isSelected = selectedDate == day.date,
                        events = state,
                    ) { day1 ->
                        selectedDate = day1.date
                    }

                },
                monthHeader = {
                    MonthHeader(calendarMonth = it)
                },
            )
        }
    }
}


@Composable
private fun MonthHeader(calendarMonth: CalendarMonth) {
    val daysOfWeek = calendarMonth.weekDays.first().map { it.date.dayOfWeek }
    Column(
        modifier = Modifier.wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = calendarMonth.yearMonth.month.name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            for (dayOfWeek in daysOfWeek) {
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp,
                    text = dayOfWeek.name.first().toString(),
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

@Composable
fun Day(
    day: CalendarDay,
    isSelected: Boolean,
    events: List<MealBookEntry> = emptyList(),
    onClick: (CalendarDay) -> Unit
) {
    val month: Month = day.date.month
    val eventsMap = events.convertToEventMap(
        year = day.date.year,
        month = month
    )
    val dayOfMonth = day.date.dayOfMonth

    // Find events for the current day
    val noOfEvent = eventsMap.entries
        .find { (_, eventsList) ->
            eventsList.any { entry ->
                val entryCal = Calendar.getInstance().apply { timeInMillis = entry.createdAt }
                entryCal.get(Calendar.DAY_OF_MONTH) == dayOfMonth
            }
        }
        ?.key ?: 0

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else Color.Transparent
            )
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) }
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = day.date.dayOfMonth.toString(),
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                else if (day.position == DayPosition.MonthDate) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.onSurface.copy(
                    alpha = .4f
                )
            )

            if (noOfEvent > 0 && day.position == DayPosition.MonthDate) {
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.primary
                            )
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = (noOfEvent.takeIf { it > 1 } ?: "").toString(),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 8.sp,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}


fun List<MealBookEntry>.convertToEventMap(year: Int, month: Month): Map<Int, List<MealBookEntry>> =
    this
        .filter {
            val entryDate = Instant.fromEpochMilliseconds(it.createdAt)
                .toLocalDateTime(TimeZone.currentSystemDefault()).date

            entryDate.year == year && entryDate.month == month
        }
        .groupBy { entry ->
            Instant.fromEpochMilliseconds(entry.createdAt)
                .toLocalDateTime(TimeZone.currentSystemDefault()).date.dayOfMonth
        }
        .entries
        .associateBy({ it.value.size }, { it.value })

fun generateMealBookEntries(): List<MealBookEntry> =
    listOf(
        MealBookEntry(10.0, "Breakfast", "1", createdAt = 1740814713000), // March 2, 2024
        MealBookEntry(20.0, "Lunch", "2", createdAt = 1740814713000), // March 2, 2024
        MealBookEntry(15.0, "Dinner", "3", createdAt = 1740901113000),  // March 3, 2024,

        MealBookEntry(15.0, "Dinner", "3", createdAt = 1738481913000), // Feb 2, 2024
        MealBookEntry(15.0, "Dinner", "3", createdAt = 1738481913000), // Feb 2, 2024
        MealBookEntry(15.0, "Dinner", "3", createdAt = 1735717113000)  // Jan 1, 2024
    )


@Preview
@Composable
private fun ViewMealScreenPreview() {
    ExpenseSyncTheme {
        ViewMealScreen(
            mealBookName = "Test", state = generateMealBookEntries()
        )
    }
}