package com.atech.expensesync.ui.screens.splitv2.details.compose.settleUp

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.atech.expensesync.database.models.GroupMember
import com.atech.expensesync.database.models.TransactionGlobalModel

@Composable
fun SettleUpScreen(
    modifier: Modifier = Modifier,
    groupMembers: List<GroupMember> = emptyList(),
    globalTransaction: List<TransactionGlobalModel> = emptyList()
) {
    LazyColumn(
        modifier = modifier
    ) {
        item(key = "GlobalTransaction") {
            GlobalTransactionItem(
                groupMembers = groupMembers,
                globalTransactions = globalTransaction
            )
        }
    }
}