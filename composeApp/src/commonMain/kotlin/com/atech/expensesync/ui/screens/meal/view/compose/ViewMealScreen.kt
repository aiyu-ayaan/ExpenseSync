package com.atech.expensesync.ui.screens.meal.view.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Today
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atech.expensesync.component.DisplayCard
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.component.TitleComposable
import com.atech.expensesync.database.room.meal.MealBookEntry
import com.atech.expensesync.ui.screens.meal.view.CalendarMonthInternal
import com.atech.expensesync.ui.screens.meal.view.ViewMealState
import com.atech.expensesync.ui.screens.meal.view.toInternal
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.DeviceType
import com.atech.expensesync.ui_utils.getDisplayType
import com.atech.expensesync.utils.DatePattern
import com.atech.expensesync.utils.convertToDateFormat
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
import com.pushpal.jetlime.ItemsList
import com.pushpal.jetlime.JetLimeColumn
import com.pushpal.jetlime.JetLimeDefaults
import com.pushpal.jetlime.JetLimeEventDefaults
import com.pushpal.jetlime.JetLimeExtendedEvent
import com.pushpal.jetlime.VerticalAlignment
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.Calendar


@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun ViewMealScreen(
    modifier: Modifier = Modifier,
    mealBookName: String,
    state: List<MealBookEntry> = emptyList(),
    calenderMonth: CalendarMonthInternal,
    onEvent: (ViewMealState) -> Unit = {},
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
    LaunchedEffect(calendarState.firstVisibleMonth) {
        onEvent.invoke(
            ViewMealState.SetCalendarMonth(
                calendarMonth = calendarState.firstVisibleMonth.toInternal()
            )
        )
    }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showHistoryBottomSheet by remember { mutableStateOf(false) }

    MainContainer(
        modifier = modifier, title = mealBookName, onNavigationClick = onNavigateUp, actions = {
            IconButton(
                enabled = calendarState.firstVisibleMonth.yearMonth.month != currentMonth.month,
                onClick = {
                    scope.launch {
                        calendarState.animateScrollToMonth(currentMonth)
                        selectedDate = LocalDate.now()
                    }
                }) {
                Icon(imageVector = Icons.TwoTone.Today, contentDescription = null)
            }
        }) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
                .padding(MaterialTheme.spacing.medium).verticalScroll(
                    rememberScrollState()
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            TitleComposable(
                title = "Calender",
            )
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
                    )
                },
                monthHeader = { month ->
                    MonthHeader(calendarMonth = month)
                })
            TextButton(
                modifier = Modifier.fillMaxWidth(), onClick = {
                    scope.launch {
                        sheetState.show()
                        showHistoryBottomSheet = true
                    }
                }) {
                Text("View History")
            }
        }
        AnimatedVisibility(
            showHistoryBottomSheet
        ) {
            ViewHistory(
                modifier = Modifier.fillMaxWidth(), sheetState = sheetState, onDismissRequest = {
                    scope.launch {
                        sheetState.hide()
                        showHistoryBottomSheet = false
                    }
                }, state = state, calenderMonth = calenderMonth
            )
        }
    }
}


@OptIn(ExperimentalComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun ViewHistory(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    onDismissRequest: () -> Unit = {},
    state: List<MealBookEntry>,
    calenderMonth: CalendarMonthInternal
) {
    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        ) {
            TitleComposable(
                "${
                    calenderMonth.yearMonth.month.name.lowercase()
                        .replaceFirstChar { it.uppercase() }
                } ${calenderMonth.yearMonth.year}",
            )
            val eventsMap = state.convertToDateMap(
                year = calenderMonth.yearMonth.year, month = calenderMonth.yearMonth.month
            )
            if (eventsMap.isEmpty()) {
                DisplayCard(
                    modifier = Modifier.padding(top = MaterialTheme.spacing.medium)
                ) {
                    Text(
                        "No entries found",
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
                return@Column
            }
            JetLimeColumn(
                modifier = Modifier.padding(16.dp),
                itemsList = ItemsList(eventsMap.keys.toList().reversed()),
                style = JetLimeDefaults.columnStyle(
                    contentDistance = 32.dp,
                    itemSpacing = 16.dp,
                    lineThickness = 2.dp,
                    lineBrush = JetLimeDefaults.lineSolidBrush(color = MaterialTheme.colorScheme.primary),
                    lineVerticalAlignment = VerticalAlignment.RIGHT,
                ),
            ) { _, item, position ->
                JetLimeExtendedEvent(
                    style = JetLimeEventDefaults.eventStyle(
                        position = position
                    ), additionalContent = {
                        ExtendedEventContent(item)
                    }) {
                    Column {
                        val getItem = eventsMap[item] ?: emptyList()
                        VerticalEventContent(getItem)
                    }
                }
            }
            Spacer(
                modifier = Modifier.height(MaterialTheme.spacing.bottomPadding)
            )
        }
    }
}

@Composable
fun ExtendedEventContent(item: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.wrapContentHeight(),
    ) {
        Column(
            modifier = Modifier.wrapContentWidth().padding(MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                text = item
            )
        }
    }
}

@Composable
fun VerticalEventContent(item: List<MealBookEntry>, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
    ) {
        Column {
            item.forEach { meal ->
                if (meal.description.isNotEmpty()) Text(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight()
                        .padding(horizontal = 12.dp).padding(top = 8.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    text = meal.description,
                )
                Text(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    fontSize = if (meal.description.isNotEmpty()) 14.sp else 16.sp,
                    text = meal.createdAt.convertToDateFormat(DatePattern.HH_MM_A),
                )
            }
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
            text = calendarMonth.yearMonth.month.name.lowercase()
                .replaceFirstChar { it.uppercase() } + if (calendarMonth.yearMonth.year == YearMonth.now().year) ""
            else " ${calendarMonth.yearMonth.year}",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall)
        Row(modifier = Modifier.fillMaxWidth()) {
            for (dayOfWeek in daysOfWeek) {
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    text = dayOfWeek.name.first().toString(),
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium
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
) {
    val month: Month = day.date.month
    val eventsMap = events.convertToEventMap(
        year = day.date.year, month = month
    )
    val dayOfMonth = day.date.dayOfMonth

    val noOfEvent = eventsMap.entries.find { (_, eventsList) ->
        eventsList.any { entry ->
            val entryCal = Calendar.getInstance().apply { timeInMillis = entry.createdAt }
            entryCal.get(Calendar.DAY_OF_MONTH) == dayOfMonth
        }
    }?.key ?: 0

    Box(
        modifier = Modifier.aspectRatio(1f).clip(CircleShape).background(
            color = if (isSelected) MaterialTheme.colorScheme.primary.copy(
                alpha = .6f
            )
            else Color.Transparent
        ), contentAlignment = Alignment.Center
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
                        modifier = Modifier.size(6.dp).clip(CircleShape).background(
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = (noOfEvent.takeIf { it > 1 } ?: "").toString(),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 8.sp,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}


fun List<MealBookEntry>.convertToEventMap(year: Int, month: Month): Map<Int, List<MealBookEntry>> =
    this.filter {
        val entryDate = Instant.fromEpochMilliseconds(it.createdAt)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date

        entryDate.year == year && entryDate.month == month
    }.groupBy { entry ->
        Instant.fromEpochMilliseconds(entry.createdAt)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date.dayOfMonth
    }.entries.associateBy({ it.value.size }, { it.value })


fun List<MealBookEntry>.convertToDateMap(
    year: Int, month: Month
): Map<String, List<MealBookEntry>> = this.mapNotNull { entry ->
    val entryDate = Instant.fromEpochMilliseconds(entry.createdAt)
        .toLocalDateTime(TimeZone.currentSystemDefault()).date

    // Filter only entries from the given month and year
    if (entryDate.year == year && entryDate.month == month) {
        val key = "${entryDate.dayOfMonth} ${
            entryDate.month.name.lowercase().replaceFirstChar { it.uppercase() }
        }"
        key to entry
    } else {
        null
    }
}.groupBy({ it.first }, { it.second }) // Group by the formatted date string


//fun generateMealBookEntries(): List<MealBookEntry> = listOf(
//    MealBookEntry(10.0, "Breakfast", "1", createdAt = 1740814713000), // March 2, 2024
//    MealBookEntry(10.0, "Breakfast", "1", createdAt = 1740814713000), // March 2, 2024
//    MealBookEntry(10.0, "Breakfast", "1", createdAt = 1740814713000), // March 2, 2024
//    MealBookEntry(10.0, "Breakfast", "1", createdAt = 1740814713000), // March 2, 2024
//    MealBookEntry(10.0, "Breakfast", "1", createdAt = 1740814713000), // March 2, 2024
//    MealBookEntry(10.0, "Breakfast", "1", createdAt = 1740814713000), // March 2, 2024
//    MealBookEntry(10.0, "Breakfast", "1", createdAt = 1740814713000), // March 2, 2024
//    MealBookEntry(10.0, "Breakfast", "1", createdAt = 1740814713000), // March 2, 2024
//    MealBookEntry(20.0, "Lunch", "2", createdAt = 1740814713000), // March 2, 2024
//    MealBookEntry(15.0, "Dinner", "3", createdAt = 1740901113000),  // March 3, 2024,
//
//    MealBookEntry(15.0, "Dinner", "3", createdAt = 1738481913000), // Feb 2, 2024
//    MealBookEntry(15.0, "Dinner", "3", createdAt = 1738481913000), // Feb 2, 2024
//    MealBookEntry(15.0, "Dinner", "3", createdAt = 1735717113000)  // Jan 1, 2024
//)

