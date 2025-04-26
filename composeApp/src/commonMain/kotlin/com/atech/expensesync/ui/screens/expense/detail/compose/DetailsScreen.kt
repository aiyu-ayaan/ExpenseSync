package com.atech.expensesync.ui.screens.expense.detail.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingDown
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.Remove
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.atech.expensesync.component.AppButtonWithIcon
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.room.expense.ExpenseBook
import com.atech.expensesync.database.room.expense.ExpenseBookEntry
import com.atech.expensesync.ui.screens.expense.root.ExpenseEvents
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.atech.expensesync.ui.theme.appGreen
import com.atech.expensesync.ui.theme.appRed
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.formatAmount
import com.atech.expensesync.utils.convertToDateFormat
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ExpenseDetailsScreen(
    modifier: Modifier = Modifier,
    state: ExpenseBook,
    expenseBookEntry: Map<Long, List<ExpenseBookEntry>>,
    onNavigateBack: () -> Unit = {},
    cashInOutClick: (CashType) -> Unit = {},
    onEvent: (ExpenseEvents) -> Unit = {}
) {
    onEvent.invoke(ExpenseEvents.LoadExpenseBookEntry)
    MainContainer(
        modifier = modifier,
        title = state.bookName,
        onNavigationClick = onNavigateBack,
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier
                        .padding(
                            MaterialTheme.spacing.small
                        ),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                ) {
                    AppButtonWithIcon(
                        modifier = Modifier.weight(1f),
                        text = "Cash In",
                        icon = Icons.TwoTone.Add,
                        colors = ButtonDefaults.buttonColors()
                            .copy(
                                containerColor = MaterialTheme.colorScheme.appGreen,
                                contentColor = Color.White
                            ),
                        shape = RoundedCornerShape(MaterialTheme.spacing.medium),
                        innerPadding = MaterialTheme.spacing.small,
                        onClick = {
                            cashInOutClick(CashType.CASH_IN)
                        }
                    )
                    AppButtonWithIcon(
                        modifier = Modifier.weight(1f),
                        icon = Icons.TwoTone.Remove,
                        text = "Cash Out",
                        colors = ButtonDefaults.buttonColors()
                            .copy(
                                containerColor = MaterialTheme.colorScheme.appRed,
                                contentColor = Color.White
                            ),
                        shape = RoundedCornerShape(MaterialTheme.spacing.medium),
                        innerPadding = MaterialTheme.spacing.small,
                        onClick = {
                            cashInOutClick(CashType.CASH_OUT)
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(
                MaterialTheme.spacing.medium
            ),
            contentPadding = paddingValues
        ) {
            item(
                key = "netBalance"
            ) {
                TopNetBalanceView(
                    expenseBook = state
                )
            }
            item(
                key = "expenseBookEntry"
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(Modifier.height(MaterialTheme.spacing.medium))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.medium),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Expense Entries",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Rounded.FilterList,
                                contentDescription = "Filter Entries"
                            )
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                        thickness = 1.dp
                    )
                    Spacer(Modifier.height(MaterialTheme.spacing.small))
                }
            }
            expenseBookEntry.forEach { date, entries ->
                stickyHeader(key = date) {
                    Surface(
                        color = MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = MaterialTheme.spacing.medium,
                                        vertical = MaterialTheme.spacing.small
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    modifier = Modifier.padding(end = MaterialTheme.spacing.small)
                                ) {
                                    Text(
                                        text = date.convertToDateFormat(),
                                        style = MaterialTheme.typography.labelMedium,
                                        modifier = Modifier.padding(
                                            vertical = MaterialTheme.spacing.extraSmall,
                                            horizontal = MaterialTheme.spacing.small
                                        ),
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }

                                HorizontalDivider(
                                    modifier = Modifier.weight(1f),
                                    thickness = 0.5.dp,
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                            }
                        }
                    }
                }
                items(entries) {
                    ExpenseBookEntryItem(
                        state = it,
                    )
                }
            }
        }
    }
}


@Composable
fun TopNetBalanceView(
    modifier: Modifier = Modifier,
    expenseBook: ExpenseBook
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.small
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium)
        ) {
            // Header with title and balance
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AccountBalance,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "Net Balance",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "${expenseBook.netBalance.formatAmount()} ${expenseBook.defaultCurrency.symbol}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = when {
                        expenseBook.netBalance > 0 -> MaterialTheme.colorScheme.appGreen
                        expenseBook.netBalance < 0 -> MaterialTheme.colorScheme.appRed
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            // Income row with icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.appGreen.copy(alpha = 0.15f),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.TrendingUp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.appGreen,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                    Text(
                        "Total In (+)",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Text(
                    text = "${expenseBook.totalIn.formatAmount()} ${expenseBook.defaultCurrency.symbol}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.appGreen
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            // Expense row with icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.appRed.copy(alpha = 0.15f),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.TrendingDown,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.appRed,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                    Text(
                        "Total Out (-)",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Text(
                    text = "${expenseBook.totalOut.formatAmount()} ${expenseBook.defaultCurrency.symbol}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.appRed
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            // Visual balance indicator
            LinearProgressIndicator(
                progress = {
                    val total = expenseBook.totalIn + expenseBook.totalOut.absoluteValue
                    val ratio = if (total == 0.0) 0.0 else (expenseBook.totalIn / total)
                    ratio.coerceIn(0.0, 1.0).toFloat()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.appGreen,
                trackColor = MaterialTheme.colorScheme.appRed.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            // Income/expense percentage indicators

            val total = expenseBook.totalIn + expenseBook.totalOut.absoluteValue

            val incomePercentage = if (total != 0.0) {
                ((expenseBook.totalIn / total) * 100).roundToInt()
            } else {
                0
            }

            val expensesPercentage = if (total != 0.0) {
                ((expenseBook.totalOut.absoluteValue / total) * 100).roundToInt()
            } else {
                0
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Income ${incomePercentage}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.appGreen
                )

                Text(
                    text = "Expenses ${expensesPercentage}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.appRed
                )
            }
        }
    }
}

@Preview
@Composable
private fun ExpenseDetailsScreenPreview() {
    ExpenseSyncTheme {
        TopNetBalanceView(
            expenseBook = ExpenseBook(
                bookName = "Budget 2024",
                totalAmount = 5000.0,
                totalIn = 300.0,
                totalOut = 200.0
            )
        )
    }
}