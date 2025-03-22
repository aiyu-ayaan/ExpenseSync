package com.atech.expensesync.ui.screens.expense.detail.compose

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.atech.expensesync.component.AppButtonWithIcon
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.room.expense.ExpenseBook
import com.atech.expensesync.ui.screens.expense.root.ExpanseEvents
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.atech.expensesync.ui.theme.appGreen
import com.atech.expensesync.ui.theme.appRed
import com.atech.expensesync.ui.theme.spacing
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDetailsScreen(
    modifier: Modifier = Modifier,
    state: ExpenseBook,
    onNavigateBack: () -> Unit = {},
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
                        innerPadding = MaterialTheme.spacing.small
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
                        innerPadding = MaterialTheme.spacing.small
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
            repeat(100) {
                item(
                    key = it
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth()
                            .padding(MaterialTheme.spacing.medium),
                        text = "Item $it",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ExpenseDetailsScreenPreview() {
    ExpenseSyncTheme {
        ExpenseDetailsScreen(
            state = ExpenseBook(
                "Book Name",

                )
        )
    }
}