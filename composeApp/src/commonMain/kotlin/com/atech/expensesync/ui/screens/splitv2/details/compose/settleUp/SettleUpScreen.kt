package com.atech.expensesync.ui.screens.splitv2.details.compose.settleUp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.atech.expensesync.component.BottomPadding
import com.atech.expensesync.database.models.GroupMember
import com.atech.expensesync.database.models.SplitTransaction
import com.atech.expensesync.database.models.TransactionGlobalModel
import com.atech.expensesync.ui.theme.spacing

@Composable
fun SettleUpScreen(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    groupMembers: List<GroupMember> = emptyList(),
    globalTransaction: List<TransactionGlobalModel> = emptyList(),
    splitTransactions: List<SplitTransaction> = emptyList()
) {
    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        item(key = "GlobalTransaction") {
            GlobalTransactionItem(
                groupMembers = groupMembers,
                globalTransactions = globalTransaction
            )
        }
        item(
            key = "Divider"
        ) {
            HorizontalDivider(
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
        }

        item(key = "TransactionHeader") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Transaction Details",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${splitTransactions.size} items",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        items(splitTransactions) {
            SplitTransactionItem(
                transaction = it,
            )
        }
        item(key = "BottomPadding") {
            BottomPadding(
                padding = MaterialTheme.spacing.bottomPadding * 2
            )
        }
    }
}