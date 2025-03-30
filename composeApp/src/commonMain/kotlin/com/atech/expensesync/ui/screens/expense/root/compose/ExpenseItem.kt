package com.atech.expensesync.ui.screens.expense.root.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.atech.expensesync.component.DefaultCard
import com.atech.expensesync.component.expenseIcons
import com.atech.expensesync.database.room.expense.ExpenseBook
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.atech.expensesync.ui.theme.appGreen
import com.atech.expensesync.ui.theme.appRed
import com.atech.expensesync.ui_utils.formatAmount
import com.atech.expensesync.utils.convertToDateFormat
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ExpenseItem(
    modifier: Modifier = Modifier,
    expenseBook: ExpenseBook,
    onClick: () -> Unit = {}
) {
    DefaultCard(
        modifier = modifier.fillMaxWidth()
            .clickable {
                onClick()
            },
    ) {
        ListItem(
            modifier = Modifier,
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            ),
            headlineContent = {
                Text(expenseBook.bookName)
            },
            supportingContent = {
                Text(
                    when {
                        expenseBook.updatedAt != null -> "Updated at: ${expenseBook.updatedAt!!.convertToDateFormat()}"
                        else -> "Created at: ${expenseBook.createdAt.convertToDateFormat()}"
                    }
                )
            },
            trailingContent = {
                AnimatedVisibility(
                    visible = expenseBook.totalAmount != 0.0
                ) {
                    Text(
                        text = "${expenseBook.netBalance.formatAmount()} ${expenseBook.defaultCurrency.symbol}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (expenseBook.netBalance >= 0) MaterialTheme.colorScheme.appGreen else MaterialTheme.colorScheme.appRed
                    )
                }
            },
            leadingContent = {
                val icon =
                    (expenseIcons.find { it.displayName == expenseBook.icon }
                        ?: expenseIcons.first()).icon
                Icon(
                    imageVector = icon,
                    contentDescription = "Meal Book"
                )
            }
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