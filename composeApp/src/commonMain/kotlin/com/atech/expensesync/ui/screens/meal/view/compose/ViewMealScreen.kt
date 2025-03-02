package com.atech.expensesync.ui.screens.meal.view.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Today
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aay.compose.baseComponents.model.GridOrientation
import com.aay.compose.lineChart.LineChart
import com.aay.compose.lineChart.model.LineParameters
import com.aay.compose.lineChart.model.LineType
import com.atech.expensesync.component.DisplayCard
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.component.TitleComposable
import com.atech.expensesync.database.room.meal.MealBookEntry
import com.atech.expensesync.ui.screens.meal.editmealbook.EditMealBookDialog
import com.atech.expensesync.ui.screens.meal.view.CalendarMonthInternal
import com.atech.expensesync.ui.screens.meal.view.ViewMealEvents
import com.atech.expensesync.ui.screens.meal.view.toInternal
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.DeviceType
import com.atech.expensesync.ui_utils.formatAmount
import com.atech.expensesync.ui_utils.getDisplayType
import com.atech.expensesync.ui_utils.showToast
import com.atech.expensesync.utils.Currency
import com.atech.expensesync.utils.DatePattern
import com.atech.expensesync.utils.convertToDateFormat
import com.atech.expensesync.utils.expenseSyncLogger
import com.atech.expensesync.utils.firstDayOfWeek
import com.atech.expensesync.utils.generatePriceSumOfBasicOfWeek
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.YearMonth
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
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import java.util.Calendar


@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun ViewMealScreen(
    modifier: Modifier = Modifier,
    mealBookName: String,
    defaultCurrency: Currency = Currency.INR,
    state: List<MealBookEntry>,
    calenderMonth: CalendarMonthInternal,
    onEvent: (ViewMealEvents) -> Unit = {},
    onNavigateUp: () -> Unit = {}
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    val firstDayOfWeek = remember { firstDayOfWeek() }
    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstDayOfWeek = firstDayOfWeek,
        firstVisibleMonth = currentMonth
    )
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var isPriceDialogVisible by remember { mutableStateOf(false) }
    var mealBookEntry: MealBookEntry? by remember { mutableStateOf(null) }
    LaunchedEffect(calendarState.firstVisibleMonth) {
        onEvent.invoke(
            ViewMealEvents.SetCalendarMonth(
                calendarMonth = calendarState.firstVisibleMonth.toInternal()
            )
        )
    }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showHistoryBottomSheet by remember { mutableStateOf(false) }

    MainContainer(
        modifier = modifier
            .nestedScroll(
                scrollBehavior.nestedScrollConnection
            ),
        scrollBehavior = scrollBehavior,
        title = mealBookName,
        onNavigationClick = onNavigateUp,
        actions = {
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
        }
    ) { paddingValues ->
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
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    showHistoryBottomSheet = true
                }) {
                Text("View History")
            }
            ShowLineChart(
                state = state,
                month = calenderMonth.yearMonth.month,
                defaultCurrency = defaultCurrency
            )
        }
        AnimatedVisibility(
            showHistoryBottomSheet
        ) {
            ViewHistory(
                modifier = Modifier.fillMaxWidth(),
                sheetState = sheetState,
                onDismissRequest = {
                    showHistoryBottomSheet = false
                },
                state = state,
                calenderMonth = calenderMonth,
                defaultCurrency = defaultCurrency,
                onItemClick = {
                    mealBookEntry = it
                    isPriceDialogVisible = !isPriceDialogVisible
                }
            )
        }
        AnimatedVisibility(
            isPriceDialogVisible && mealBookEntry != null
        ) {
            EditMealBookDialog(
                isEdit = true,
                price = mealBookEntry?.price?.formatAmount() ?: 0.0.formatAmount(),
                currency = defaultCurrency,
                date = mealBookEntry?.createdAt ?: System.currentTimeMillis(),
                onDismissRequest = {
                    isPriceDialogVisible = false
                    mealBookEntry = null
                }, confirmButton = { it ->
                    onEvent.invoke(
                        ViewMealEvents.UpdateMealBookEntry(
                            oldMealBookEntry = mealBookEntry,
                            mealBookEntry = it.copy(
                                mealBookId = mealBookEntry?.mealBookId
                                    ?: return@EditMealBookDialog,
                            ), onComplete = { request ->
                                if (request < 0) {
                                    showToast(
                                        "Failed to create Meal Book Entry"
                                    )
                                    return@UpdateMealBookEntry
                                }
                                showToast(
                                    "Meal Book Entry created successfully"
                                )
                                isPriceDialogVisible = false
                                mealBookEntry = null
                            })
                    )
                },
                onDeleteItem = {
                    onEvent.invoke(
                        ViewMealEvents.OnDeleteMealBookEntry(
                            mealBookEntry = mealBookEntry ?: return@EditMealBookDialog
                        )
                    )
                    isPriceDialogVisible = false
                    mealBookEntry = null
                }
            )
        }
    }
}

@Composable
fun ShowLineChart(
    modifier: Modifier = Modifier,
    state: List<MealBookEntry>,
    month: Month,
    defaultCurrency: Currency = Currency.INR
) {
    val (monthPrev, monthCurr) = state.generatePriceSumOfBasicOfWeek(month)
    val monthCurrSum = monthCurr.sum()
    val monthPrevSum = monthPrev.sum()
    AnimatedVisibility(
        (monthPrevSum == 0.0 && monthCurrSum == 0.0).not()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            TitleComposable("Summary")
            val lineChartData = listOf(
                LineParameters(
                    label = month.name.lowercase().replaceFirstChar { it.uppercase() },
                    lineColor = MaterialTheme.colorScheme.primary,
                    lineType = LineType.CURVED_LINE,
                    lineShadow = true,
                    data = monthCurr
                ),
                LineParameters(
                    label = if (month == Month.JANUARY) Month.DECEMBER.name.lowercase()
                        .replaceFirstChar { it.uppercase() }
                    else Month.of(month.number - 1).name.lowercase()
                        .replaceFirstChar { it.uppercase() },
                    data = monthPrev,
                    lineColor = MaterialTheme.colorScheme.inversePrimary,
                    lineType = LineType.CURVED_LINE,
                    lineShadow = true
                )
            )
            LineChart(
                modifier = modifier
                    .fillMaxSize()
                    .aspectRatio(1f),
                linesParameters = lineChartData,
                isGrid = false,
                xAxisData = List(monthCurr.size) { index -> "Week ${index + 1}" },
                animateChart = true,
                yAxisStyle = TextStyle(
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = .6f
                    ),
                ),
                xAxisStyle = TextStyle(
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                        .copy(
                            alpha = .6f
                        ),
                    fontWeight = FontWeight.W400
                ),
                yAxisRange = 14,
                oneLineChart = false,
                gridOrientation = GridOrientation.VERTICAL,
                descriptionStyle = TextStyle(
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.W400
                ),
            )
            DisplayCard(
                modifier = Modifier.padding(top = MaterialTheme.spacing.medium)
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            vertical = MaterialTheme
                                .spacing.medium
                        ),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                ) {
                    AnimatedVisibility(
                        monthCurrSum != 0.0
                    ) {
                        Text(
                            text = "Current: ${defaultCurrency.symbol} ${monthCurrSum.formatAmount()}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    AnimatedVisibility(
                        monthPrevSum != 0.0
                    ) {
                        Text(
                            text = "Previous: ${defaultCurrency.symbol} ${monthPrevSum.formatAmount()}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(MaterialTheme.spacing.bottomPadding)
            )
        }
    }
}


@OptIn(ExperimentalComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun ViewHistory(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    defaultCurrency: Currency,
    onDismissRequest: () -> Unit = {},
    state: List<MealBookEntry>,
    calenderMonth: CalendarMonthInternal,
    onItemClick: (MealBookEntry) -> Unit
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
                itemsList = ItemsList(eventsMap.keys.toList()),
                style = JetLimeDefaults.columnStyle(
                    contentDistance = MaterialTheme.spacing.extraLarge,
                    itemSpacing = MaterialTheme.spacing.large,
                    lineThickness = MaterialTheme.spacing.extraSmall,
                    lineBrush = JetLimeDefaults.lineSolidBrush(color = MaterialTheme.colorScheme.primary),
                    lineVerticalAlignment = VerticalAlignment.RIGHT,
                ),
            ) { _, item, position ->
                JetLimeExtendedEvent(
                    style = JetLimeEventDefaults.eventStyle(
                        position = position
                    ), additionalContent = {
                        ExtendedEventContent(
                            item,
                            total = eventsMap[item]?.sumOf { it.price } ?: 0.0,
                            defaultCurrency = defaultCurrency
                        )
                    }) {
                    Column {
                        val getItem = eventsMap[item] ?: emptyList()
                        VerticalEventContent(
                            getItem,
                            defaultCurrency,
                            onItemClick = onItemClick
                        )
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
fun ExtendedEventContent(
    item: String,
    total: Double = 0.0,
    defaultCurrency: Currency,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.wrapContentHeight()
            .padding(
                bottom = MaterialTheme.spacing.large
            ),
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
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            Text(
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                text = "Total:\n${defaultCurrency.symbol} ${total.formatAmount()}",
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun VerticalEventContent(
    item: List<MealBookEntry>,
    defaultCurrency: Currency = Currency.INR,
    modifier: Modifier = Modifier,
    onItemClick: (MealBookEntry) -> Unit
) {
    Card(
        modifier = modifier,
    ) {
        val maxIndex = item.size - 1
        Column {
            item.forEachIndexed { index, meal ->
                Column {
                    ListItem(
                        modifier = Modifier
                            .clickable {
                                onItemClick(meal)
                            },
                        headlineContent = {
                            Text(
                                text = "${defaultCurrency.symbol} ${meal.price.formatAmount()}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        supportingContent = if (meal.description.isNotEmpty()) {
                            {
                                Text(
                                    text = meal.description,
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier
                                        .basicMarquee(
                                        )
                                )
                            }
                        } else null,
                        trailingContent = {
                            Text(
                                text = meal.createdAt.convertToDateFormat(DatePattern.HH_MM_A),
                                style = MaterialTheme.typography.labelMedium,
                                maxLines = 1
                            )
                        }
                    )
                    if (maxIndex != 0 && index != maxIndex)
                        HorizontalDivider()
                }
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
    expenseSyncLogger(
        "$eventsMap"
    )
    val dayOfMonth = day.date.dayOfMonth
    val noOfEvent = eventsMap[dayOfMonth]?.size ?: 0

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


fun List<MealBookEntry>.convertToEventMap(year: Int, month: Month): Map<Int, List<MealBookEntry>> {
    // Create a Calendar instance to work with dates
    val calendar = Calendar.getInstance()

    // Filter entries that match the specified year and month
    val filteredEntries = this.filter { entry ->
        calendar.timeInMillis = entry.createdAt
        val entryYear = calendar.get(Calendar.YEAR)
        val entryMonth = calendar.get(Calendar.MONTH)

        // Month in Calendar is 0-based (January is 0), while Month enum is typically 1-based
        entryYear == year && entryMonth == (month.ordinal)
    }

    // Group entries by day of month
    return filteredEntries.groupBy { entry ->
        calendar.timeInMillis = entry.createdAt
        calendar.get(Calendar.DAY_OF_MONTH)
    }
}


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


fun generateMealBookEntries(): List<MealBookEntry> = listOf(
    MealBookEntry(1.0, "Breakfast", "1", createdAt = 1740814713000), // March 2, 2024
    MealBookEntry(1.0, "Breakfast", "1", createdAt = 1740814713000), // March 2, 2024
    MealBookEntry(1.0, "Breakfast", "1", createdAt = 1740814713000), // March 2, 2024
    MealBookEntry(1.0, "Breakfast", "1", createdAt = 1740814713000), // March 2, 2024
    MealBookEntry(1.0, "Breakfast", "1", createdAt = 1740814713000), // March 2, 2024
    MealBookEntry(1.0, "Breakfast", "1", createdAt = 1740814713000), // March 2, 2024
    MealBookEntry(1.0, "Breakfast", "1", createdAt = 1740814713000), // March 2, 2024
    MealBookEntry(1.0, "Breakfast", "1", createdAt = 1740814713000), // March 2, 2024
    MealBookEntry(2.0, "Lunch", "2", createdAt = 1740814713000), // March 2, 2024
    MealBookEntry(1.0, "Dinner", "3", createdAt = 1740901113000),  // March 3, 2024,

    MealBookEntry(1.0, "Dinner", "3", createdAt = 1738481913000), // Feb 2, 2024
    MealBookEntry(1.0, "Dinner", "3", createdAt = 1738481913000), // Feb 2, 2024
    MealBookEntry(1.0, "Dinner", "3", createdAt = 1738481913000), // Feb 2, 2024
    MealBookEntry(1.0, "Dinner", "3", createdAt = 1738481913000), // Feb 2, 2024
    MealBookEntry(1.0, "Dinner", "3", createdAt = 1738481913000), // Feb 2, 2024
    MealBookEntry(1.0, "Dinner", "3", createdAt = 1738481913000), // Feb 2, 2024
    MealBookEntry(1.0, "Dinner", "3", createdAt = 1738481913000)  // Jan 1, 2024
)

