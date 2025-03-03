package com.atech.expensesync.ui.screens.split.add.compose.settleUpScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.icons.twotone.Money
import androidx.compose.material.icons.twotone.Pending
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.atech.expensesync.database.room.split.SplitGroupMembers
import com.atech.expensesync.database.room.split.SplitTransactions
import com.atech.expensesync.database.room.split.TransactionSplit

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettleUpScreen(
    modifier: Modifier = Modifier,
    groupMembers: List<SplitGroupMembers>,
    state: Map<SplitTransactions, List<Pair<TransactionSplit, SplitGroupMembers>>>,
    onEditClick : (SplitTransactions) -> Unit,
    onClick: (
        transaction: SplitTransactions,
        groupMembers: List<SplitGroupMembers>,
        split: List<TransactionSplit>,
    ) -> Unit
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(state.size) { index ->
            val (transaction, splits) = state.entries.elementAt(index)
            val paidBy =
                groupMembers.first { it.key == transaction.paidByKey }
            ShowTransactionItems(
                transaction = transaction,
                paidByName = paidBy.name,
                onClick = {
                    onClick(transaction, groupMembers, splits.map { it.first })
                },
                onEditClick = {
                    onEditClick(transaction)
                }
            )
        }
    }
}

@Composable
private fun ShowTransactionItems(
    modifier: Modifier = Modifier,
    paidByName: String,
    transaction: SplitTransactions,
    onEditClick : () -> Unit,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .clickable {
                onClick()
            }
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
                Text(text = "$paidByName paid ${transaction.formatedAmount}")
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
                IconButton(onClick = onEditClick) {
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
    model: Pair<TransactionSplit, SplitGroupMembers>
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
                Text(text = "${user.name} owes ${transactionSplit.formatedAmount}")
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
