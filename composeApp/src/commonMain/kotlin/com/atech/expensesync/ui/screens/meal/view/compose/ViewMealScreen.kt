package com.atech.expensesync.ui.screens.meal.view.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.DinnerDining
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.icons.twotone.FreeBreakfast
import androidx.compose.material.icons.twotone.LunchDining
import androidx.compose.material.icons.twotone.Nightlife
import androidx.compose.material.icons.twotone.Today
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.ripple
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
import com.atech.expensesync.LocalUploadDataHelper
import com.atech.expensesync.component.DisplayCard
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.component.TitleComposable
import com.atech.expensesync.database.room.meal.MealBookEntry
import com.atech.expensesync.ui.screens.meal.edit.EditMealBookEntryDialog
import com.atech.expensesync.ui.screens.meal.root.AddMealBookState
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
import kotlinx.datetime.DayOfWeek
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
    mealBookState: AddMealBookState,
    defaultCurrency: Currency = Currency.INR,
    state: List<MealBookEntry>,
    calenderMonth: CalendarMonthInternal,
    onDeleteClear: () -> Unit = {},
    onEditClick: (AddMealBookState) -> Unit = {},
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
    var deleteWarningDialogVisible by remember { mutableStateOf(false) }
    val uploadDataHelper = LocalUploadDataHelper.current



    AnimatedVisibility(deleteWarningDialogVisible) {
        AlertDialog(title = {
            Text("Delete Meal Book")
        }, text = {
            Text("Are you sure you want to delete this meal book?")
        }, onDismissRequest = { deleteWarningDialogVisible = false }, confirmButton = {
            TextButton(onClick = {
                deleteWarningDialogVisible = false
                onEvent.invoke(
                    ViewMealEvents.OnDeleteMealBook {
                        onNavigateUp()
                    })
            }) {
                Text("Delete")
            }
        }, dismissButton = {
            TextButton(onClick = {
                deleteWarningDialogVisible = false
            }) {
                Text("Cancel")
            }
        })
    }

    MainContainer(
        modifier = modifier.nestedScroll(
            scrollBehavior.nestedScrollConnection
        ),
        scrollBehavior = scrollBehavior,
        title = mealBookState.name,
        onNavigationClick = onNavigateUp,
        actions = {
            IconButton(
                onClick = {
                    onEditClick.invoke(
                        mealBookState
                    )
                }) {
                Icon(imageVector = Icons.TwoTone.Edit, contentDescription = null)
            }

            IconButton(
                onClick = {
                    deleteWarningDialogVisible = !deleteWarningDialogVisible
                }) {
                Icon(imageVector = Icons.TwoTone.Delete, contentDescription = null)
            }
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
            AnimatedVisibility(
                mealBookState.description.isNotEmpty()
            ) {
                Column {
                    TitleComposable(
                        title = "Description",
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth().padding(
                            start = MaterialTheme.spacing.small
                        ),
                        text = mealBookState.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = .8f
                        )
                    )
                }
            }
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
                })
        }
        AnimatedVisibility(
            isPriceDialogVisible && mealBookEntry != null
        ) {
            EditMealBookEntryDialog(
                isEdit = true,
                price = mealBookEntry?.price?.formatAmount() ?: 0.0.formatAmount(),
                currency = defaultCurrency,
                date = mealBookEntry?.createdAt ?: System.currentTimeMillis(),
                onDismissRequest = {
                    isPriceDialogVisible = false
                    mealBookEntry = null
                },
                confirmButton = { it ->
                    onEvent.invoke(
                        ViewMealEvents.UpdateMealBookEntry(
                            oldMealBookEntry = mealBookEntry, mealBookEntry = it.copy(
                                mealBookId = mealBookEntry?.mealBookId
                                    ?: return@EditMealBookEntryDialog,
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
                                uploadDataHelper.uploadMealData {
                                    isPriceDialogVisible = false
                                    mealBookEntry = null
                                }
                            })
                    )
                },
                onDeleteItem = {
                    onEvent.invoke(
                        ViewMealEvents.OnDeleteMealBookEntry(
                            mealBookEntry = mealBookEntry ?: return@EditMealBookEntryDialog
                        )
                    )
                    isPriceDialogVisible = false
                    mealBookEntry = null
                })
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
                modifier = when (getDisplayType()) {
                    DeviceType.MOBILE -> modifier.fillMaxWidth().aspectRatio(1f)

                    else -> modifier.fillMaxWidth(
                        .5f
                    ).aspectRatio(1f)
                },
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
                    fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = .6f
                    ), fontWeight = FontWeight.W400
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
                    modifier = Modifier.padding(
                        vertical = MaterialTheme.spacing.medium
                    ), verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
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
                            defaultCurrency = defaultCurrency)
                    }) {
                    Column {
                        val getItem = eventsMap[item] ?: emptyList()
                        VerticalEventContent(
                            getItem, defaultCurrency, onItemClick = onItemClick
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
    item: String, total: Double = 0.0, defaultCurrency: Currency, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.wrapContentHeight().padding(
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        val maxIndex = item.size - 1
        Column {
            item.forEachIndexed { index, meal ->
                ListItem(
                    modifier = Modifier
                        .clickable(
                            indication = ripple(),
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onItemClick(meal) },
                    headlineContent = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "${defaultCurrency.symbol} ${meal.price.formatAmount()}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            // Add a small indicator for meal type if available
                            if (meal.price > 0) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                )
                            }
                        }
                    },
                    supportingContent = if (meal.description.isNotEmpty()) {
                        {
                            Text(
                                text = meal.description,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.basicMarquee(),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else null,
                    trailingContent = {
                        Text(
                            text = meal.createdAt.convertToDateFormat(DatePattern.HH_MM_12),
                            style = MaterialTheme.typography.labelMedium,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    },
                    // Add leading icon based on time of day
                    leadingContent = {
                        val calendar =
                            Calendar.getInstance().apply { timeInMillis = meal.createdAt }
                        val hour = calendar.get(Calendar.HOUR_OF_DAY)
                        val icon = when {
                            hour < 11 -> Icons.TwoTone.FreeBreakfast
                            hour < 16 -> Icons.TwoTone.LunchDining
                            hour < 21 -> Icons.TwoTone.DinnerDining
                            else -> Icons.TwoTone.Nightlife
                        }

                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                        )
                    }
                )
                if (maxIndex != 0 && index != maxIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun MonthHeader(calendarMonth: CalendarMonth) {
    val daysOfWeek = calendarMonth.weekDays.first().map { it.date.dayOfWeek }
    val isCurrentMonth = calendarMonth.yearMonth == YearMonth.now()

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isCurrentMonth) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                text = calendarMonth.yearMonth.month.name.lowercase()
                    .replaceFirstChar { it.uppercase() } + if (calendarMonth.yearMonth.year == YearMonth.now().year) ""
                else " ${calendarMonth.yearMonth.year}",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (isCurrentMonth) FontWeight.Bold else FontWeight.SemiBold,
                color = if (isCurrentMonth) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 8.dp)
        ) {
            for (dayOfWeek in daysOfWeek) {
                val isWeekend = dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    text = dayOfWeek.name.first().toString(),
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isWeekend) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
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
    val eventsMap = events.convertToEventMap(year = day.date.year, month = month)
    val dayOfMonth = day.date.dayOfMonth
    val noOfEvent = eventsMap[dayOfMonth]?.size ?: 0
    val hasEvents = noOfEvent > 0
    val isToday = day.date == LocalDate.now()

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(CircleShape)
            .background(
                color = when {
                    isSelected -> MaterialTheme.colorScheme.primary
                    isToday -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                    else -> Color.Transparent
                }
            )
            .border(
                width = if (isToday && !isSelected) 1.dp else 0.dp,
                color = if (isToday && !isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = day.date.dayOfMonth.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal,
                color = when {
                    isSelected -> MaterialTheme.colorScheme.onPrimary
                    day.position == DayPosition.MonthDate -> MaterialTheme.colorScheme.onSurface
                    else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                }
            )

            if (hasEvents && day.position == DayPosition.MonthDate) {
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Create a custom indicator based on event count
                    when {
                        noOfEvent >= 3 -> {
                            // Multiple events indicator
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(1.dp)
                            ) {
                                repeat(3) {
                                    Box(
                                        modifier = Modifier
                                            .size(4.dp)
                                            .clip(CircleShape)
                                            .background(
                                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                                else MaterialTheme.colorScheme.primary
                                            )
                                    )
                                }
                            }
                        }

                        noOfEvent == 2 -> {
                            // Two events indicator
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(1.dp)
                            ) {
                                repeat(2) {
                                    Box(
                                        modifier = Modifier
                                            .size(4.dp)
                                            .clip(CircleShape)
                                            .background(
                                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                                else MaterialTheme.colorScheme.primary
                                            )
                                    )
                                }
                            }
                        }

                        else -> {
                            // Single event indicator
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                        else MaterialTheme.colorScheme.primary
                                    )
                            )
                        }
                    }
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

