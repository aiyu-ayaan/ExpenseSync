package com.atech.expensesync.ui.screens.meal.root.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.twotone.Checklist
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.atech.expensesync.component.mealIcons
import com.atech.expensesync.database.room.meal.MealBook
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.atech.expensesync.ui.theme.appGreen
import com.atech.expensesync.ui.theme.appRed
import com.atech.expensesync.ui_utils.formatAmount
import com.atech.expensesync.utils.Currency
import com.atech.expensesync.utils.getCurrentDayAndTotalDays
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.abs
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun MealItem(
    modifier: Modifier = Modifier,
    state: MealBook,
    totalPrice: Double = 0.0,
    lastMonthPrice: Double = 100.0,
    onMealItemClick: () -> Unit = {},
    onlLongClick: () -> Unit = {},
    onActionClick: () -> Unit = {}
) {
    val (current, total) = getCurrentDayAndTotalDays()
    val percentageOfMonthPassed = current.toFloat() / total.toFloat()

    OutlinedCard(
        modifier = modifier.fillMaxWidth().combinedClickable(
                onClick = onMealItemClick, onLongClick = onlLongClick
            ), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header with day progress and action button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DayProgressIndicator(current, total)

                SmallActionButton(
                    onClick = onActionClick,
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.Checklist,
                        contentDescription = "Track meal",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Meal info section
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon in circular container
                Box(
                    modifier = Modifier.size(50.dp).background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = CircleShape
                        ), contentAlignment = Alignment.Center
                ) {
                    val icon =
                        (mealIcons.find { it.displayName == state.icon } ?: mealIcons.first()).icon
                    Icon(
                        imageVector = icon,
                        contentDescription = "Meal icon",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Meal name and price
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = state.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )

                    if (state.description.isNotEmpty()) {
                        Text(
                            text = state.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AttachMoney,
                            contentDescription = "Default price",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "${state.defaultPrice.formatAmount()} ${state.defaultCurrency.symbol} / meal",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Price summary section
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Month progress bar
                LinearProgressIndicator(
                    progress = {
                        if (percentageOfMonthPassed > 1) 1f else percentageOfMonthPassed
                    },
                    modifier = Modifier.fillMaxWidth().height(6.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Billing info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when {
                        totalPrice > 0 -> {
                            BillingInfo(
                                label = "This month",
                                price = totalPrice,
                                currency = state.defaultCurrency,
                                color = MaterialTheme.colorScheme.primary
                            )

                            if (lastMonthPrice > 0) {
                                BillingInfo(
                                    label = "Last month",
                                    price = lastMonthPrice,
                                    currency = state.defaultCurrency,
                                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f)
                                )
                            }

                            // Show trend indicator if we have both current and last month data
                            if (lastMonthPrice > 0) {
                                TrendIndicator(
                                    currentValue = totalPrice,
                                    previousValue = lastMonthPrice,
                                    percentCompleted = percentageOfMonthPassed
                                )
                            }
                        }

                        lastMonthPrice > 0 && current == 1 -> {
                            BillingInfo(
                                label = "Last month",
                                price = lastMonthPrice,
                                currency = state.defaultCurrency,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }

                        else -> {
                            Text(
                                text = "No billing history yet",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DayProgressIndicator(current: Int, total: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.DateRange,
            contentDescription = "Day progress",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = "Day $current of $total",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SmallActionButton(
    onClick: () -> Unit, containerColor: Color, content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.size(40.dp).clip(CircleShape).background(containerColor)
            .clickable(onClick = onClick), contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun BillingInfo(
    label: String, price: Double, currency: Currency, color: Color
) {
    Column(
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "${price.formatAmount()} ${currency.symbol}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}

@Composable
private fun TrendIndicator(
    currentValue: Double, previousValue: Double, percentCompleted: Float
) {
    // Project the end-of-month value based on current spending pace
    val projectedValue = if (percentCompleted > 0) {
        currentValue / percentCompleted
    } else {
        currentValue
    }

    val percentChange = if (previousValue > 0) {
        ((projectedValue - previousValue) / previousValue) * 100
    } else {
        0.0
    }

    val isIncreasing = percentChange > 0
    val trendColor = when {
        isIncreasing -> MaterialTheme.colorScheme.appRed
        percentChange < 0 -> MaterialTheme.colorScheme.appGreen
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        horizontalAlignment = Alignment.End
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isIncreasing) Icons.AutoMirrored.Default.TrendingUp else Icons.AutoMirrored.Filled.TrendingDown,
                contentDescription = if (isIncreasing) "Increasing trend" else "Decreasing trend",
                tint = trendColor,
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "${abs(percentChange).roundToInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = trendColor
            )
        }

        Text(
            text = if (isIncreasing) "More than last month" else "Less than last month",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview
@Composable
private fun MealItemPreview() {
    ExpenseSyncTheme {
        MealItem(
            state = MealBook(
                name = "Breakfast", description = "Subha ka khana", defaultPrice = 70.0
            )
        )
    }
}