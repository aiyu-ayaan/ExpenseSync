package com.atech.expensesync.ui.screens.expense.root.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.atech.expensesync.component.expenseIcons
import com.atech.expensesync.database.room.expense.ExpenseBook
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.atech.expensesync.ui.theme.appGreen
import com.atech.expensesync.ui.theme.appRed
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.formatAmount
import com.atech.expensesync.utils.Currency
import com.atech.expensesync.utils.convertToDateFormat
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ExpenseItem(
    modifier: Modifier = Modifier,
    expenseBook: ExpenseBook,
    onClick: () -> Unit = {}
) {
    val gradientColors = when {
        expenseBook.netBalance > 0 -> listOf(
            MaterialTheme.colorScheme.appGreen.copy(alpha = 0.1f),
            Color.Transparent
        )

        expenseBook.netBalance < 0 -> listOf(
            MaterialTheme.colorScheme.appRed.copy(alpha = 0.1f),
            Color.Transparent
        )

        else -> listOf(Color.Transparent, Color.Transparent)
    }

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Box(
            modifier = Modifier.background(
                brush = Brush.horizontalGradient(gradientColors)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Header Row with Icon and Title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val icon = (expenseIcons.find { it.displayName == expenseBook.icon }
                            ?: expenseIcons.first()).icon

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = "Expense Icon",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Text(
                            text = expenseBook.bookName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (expenseBook.description.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Rounded.Info,
                            contentDescription = "Description",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Financial information
                AnimatedVisibility(visible = expenseBook.totalAmount != 0.0) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            FinancialItem(
                                label = "Income",
                                amount = expenseBook.totalIn,
                                currency = expenseBook.defaultCurrency,
                                color = MaterialTheme.colorScheme.appGreen
                            )

                            FinancialItem(
                                label = "Expense",
                                amount = expenseBook.totalOut,
                                currency = expenseBook.defaultCurrency,
                                color = MaterialTheme.colorScheme.appRed
                            )

                            FinancialItem(
                                label = "Balance",
                                amount = expenseBook.netBalance,
                                currency = expenseBook.defaultCurrency,
                                color = if (expenseBook.netBalance >= 0)
                                    MaterialTheme.colorScheme.appGreen
                                else
                                    MaterialTheme.colorScheme.appRed,
                                isBold = true
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                // Date information
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.DateRange,
                        contentDescription = "Date",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = when {
                            expenseBook.updatedAt != null -> "Updated: ${expenseBook.updatedAt!!.convertToDateFormat()}"
                            else -> "Created: ${expenseBook.createdAt.convertToDateFormat()}"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun FinancialItem(
    label: String,
    amount: Double,
    currency: Currency,
    color: Color,
    isBold: Boolean = false
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
            text = "${amount.formatAmount()} ${currency.symbol}",
            style = if (isBold)
                MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            else
                MaterialTheme.typography.titleMedium,
            color = color
        )
    }
}

@Preview
@Composable
private fun ExpenseItemPreview() {
    ExpenseSyncTheme {
        ExpenseItem(
            expenseBook = ExpenseBook(
                bookName = "Budget 2024"
            )
        )
    }
}