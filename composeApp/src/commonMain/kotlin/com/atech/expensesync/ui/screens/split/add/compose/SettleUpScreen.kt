package com.atech.expensesync.ui.screens.split.add.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.icons.twotone.Money
import androidx.compose.material.icons.twotone.Pending
import androidx.compose.material.icons.twotone.PendingActions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.atech.expensesync.database.room.split.ExpanseGroupMembers
import com.atech.expensesync.database.room.split.ExpanseTransactions
import com.atech.expensesync.database.room.split.TransactionSplit

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettleUpScreen(
    modifier: Modifier = Modifier,
    groupMembers: List<ExpanseGroupMembers>,
    state: Map<ExpanseTransactions, List<Pair<TransactionSplit, ExpanseGroupMembers>>>,
) {
    LazyColumn(
        modifier = modifier,
        reverseLayout = true
    ) {
        state.forEach { (transaction, splits) ->
            stickyHeader {
                val paidByName =
                    groupMembers.first { it.key == transaction.paidBy }.name
                ShowTransactionItems(
                    transaction = transaction,
                    paidByName = paidByName
                )
            }
            items(
                items = splits,
            ) { model ->
                TransactionSplitItems(
                    model = model
                )
            }
        }
    }
}

@Composable
private fun ShowTransactionItems(
    modifier: Modifier = Modifier,
    paidByName: String,
    transaction: ExpanseTransactions
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        val supportingContent: @Composable () -> Unit =
            if (transaction.description.isNotBlank()) {
                { Text(text = transaction.description) }
            } else {
                {}
            }
        ListItem(
            modifier = modifier,
            headlineContent = {
                Text(text = "$paidByName paid ${transaction.amount}")
            },
            supportingContent = supportingContent,
            overlineContent = {
                Text(text = transaction.formatedDate)
            },
            leadingContent = {
                Icon(
                    imageVector = Icons.TwoTone.Money,
                    contentDescription = null
                )
            },
            trailingContent = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.TwoTone.Edit,
                        contentDescription = null
                    )
                }
            }
        )
        HorizontalDivider()
    }
}

@Composable
private fun TransactionSplitItems(
    modifier: Modifier = Modifier,
    model: Pair<TransactionSplit, ExpanseGroupMembers>
) {
    val (transactionSplit, user) = model
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ListItem(
            modifier = modifier,
            overlineContent = {
                Text(text = transactionSplit.formatedDate)
            },
            headlineContent = {
                Text(text = "you owe ${transactionSplit.amount} to ${user.name}")
            },
            trailingContent = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.TwoTone.Pending,
                        contentDescription = null
                    )
                }
            }
        )
        HorizontalDivider()
    }
}