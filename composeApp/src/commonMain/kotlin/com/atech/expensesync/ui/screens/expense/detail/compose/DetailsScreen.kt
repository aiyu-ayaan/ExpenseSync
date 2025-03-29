package com.atech.expensesync.ui.screens.expense.detail.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.Remove
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.atech.expensesync.component.AppButtonWithIcon
import com.atech.expensesync.component.DefaultCard
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.room.expense.ExpenseBook
import com.atech.expensesync.ui.screens.expense.root.ExpanseEvents
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.atech.expensesync.ui.theme.appGreen
import com.atech.expensesync.ui.theme.appRed
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.formatAmount
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDetailsScreen(
    modifier: Modifier = Modifier,
    state: ExpenseBook,
    onNavigateBack: () -> Unit = {},
    cashInOutClick: (CashType) -> Unit = {},
    onEvent: (ExpanseEvents) -> Unit = {}
) {
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
        }
    }
}


@Composable
fun TopNetBalanceView(
    modifier: Modifier = Modifier,
    expenseBook: ExpenseBook
) {
    DefaultCard(
        modifier = modifier.fillMaxWidth()
        /*.padding(MaterialTheme.spacing.medium)*/
    ) {
        ListItem(
            modifier = modifier,
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            ),
            headlineContent = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Net Balance",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "${expenseBook.netBalance.formatAmount()} ${expenseBook.defaultCurrency.symbol}",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    HorizontalDivider()
                }
            },
            supportingContent = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(
                            top = MaterialTheme.spacing.medium
                        ),
                    verticalArrangement = Arrangement.spacedBy(
                        MaterialTheme.spacing.medium
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Total In (+)",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "${expenseBook.totalIn.formatAmount()} ${expenseBook.defaultCurrency.symbol}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.appGreen
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Total Out (-)",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "${expenseBook.totalOut.formatAmount()} ${expenseBook.defaultCurrency.symbol}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.appRed
                        )
                    }
                }
            }
        )
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