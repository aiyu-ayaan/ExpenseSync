package com.atech.expensesync.ui.screens.split.add.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AddAPhoto
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.icons.twotone.Money
import androidx.compose.material.icons.twotone.Pending
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.aay.compose.donutChart.DonutChart
import com.aay.compose.donutChart.model.PieChartData
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.room.split.ExpanseGroupMembers
import com.atech.expensesync.database.room.split.ExpanseTransactions
import com.atech.expensesync.database.room.split.TransactionSplit
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.generateHarmonizedColors
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettleUpScreen(
    modifier: Modifier = Modifier,
    groupMembers: List<ExpanseGroupMembers>,
    state: Map<ExpanseTransactions, List<Pair<TransactionSplit, ExpanseGroupMembers>>>,
    onClick: (
        groupMembers: List<ExpanseGroupMembers>,
        split: List<TransactionSplit>,
    ) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        reverseLayout = true
    ) {
        state.forEach { (transaction, splits) ->
            stickyHeader {
                val paidBy =
                    groupMembers.first { it.key == transaction.paidByKey }
                ShowTransactionItems(
                    transaction = transaction,
                    paidByName = paidBy.name,
                    onClick = { onClick(groupMembers, splits.map { it.first }) }
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
    transaction: ExpanseTransactions,
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDetailOfTransaction(
    modifier: Modifier = Modifier,
    transaction: ExpanseTransactions,
    groupMembers: List<ExpanseGroupMembers>,
    split: List<TransactionSplit>,
    onNavigationClick: () -> Unit = {},
) {
    MainContainer(
        modifier = modifier,
        title = transaction.description,
        onNavigationClick = onNavigationClick,
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.TwoTone.AddAPhoto, contentDescription = null)
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.TwoTone.Delete, contentDescription = null)
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.TwoTone.Edit, contentDescription = null)
            }
        }
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium)
                .padding(paddingValue),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            Text(
                text = transaction.description,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = transaction.formatedAmount,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                "Paid by ${groupMembers.first { it.key.contains(transaction.paidByKey) }.name} on ${transaction.formatedDate}",
                style = MaterialTheme.typography.labelSmall,
            )
            CreateDonutChar(
                groupMembers = groupMembers,
                split = split
            )
        }
    }
}

@Composable
private fun CreateDonutChar(
    modifier: Modifier = Modifier,
    groupMembers: List<ExpanseGroupMembers>,
    split: List<TransactionSplit>
) {
    val colorList = generateHarmonizedColors(split.size)
    val pieChartData: List<PieChartData> =
        split.mapIndexed { index, splitTransaction ->
            val user = groupMembers.find { it.key == splitTransaction.memberKey }?.name
                ?: "No Name"
            PieChartData(
                partName = user,
                data = splitTransaction.amount,
                color = colorList[index]
            )
        }
    DonutChart(
        modifier = modifier.fillMaxSize(),
        pieChartData = pieChartData,
        centerTitle = "Split",
        centerTitleStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
        )
    )
}


//Column {
//    split.forEach { split ->
//        val user = groupMembers.first { it.key.contains(split.memberKey) }
//        Text(
//            text = "${user.name} owes ${split.formatedAmount}",
//            style = MaterialTheme.typography.bodyMedium
//        )
//    }
//}

@Preview
@Composable
private fun SettleUpScreenPreview() {
    ExpenseSyncTheme {
        ShowDetailOfTransaction(
            transaction = ExpanseTransactions(
                transactionId = "transactionID123",
                groupId = "sdf",
                amount = 300.0,
                description = "Test",
                paidByKey = "xvz$23",
            ),
            groupMembers = listOf(
                ExpanseGroupMembers(
                    uid = "xyz",
                    name = "Ayaan",
                    key = "xvz$23",
                    email = "ayaan@expanseSyncTest",
                    pic = "https://www.google.com",
                    groupId = "sdf"
                ),
                ExpanseGroupMembers(
                    uid = "abc",
                    name = "Anshu",
                    key = "abc$56",
                    email = "anshu@expanseSyncTest",
                    pic = "https://www.google.com",
                    groupId = "sdf"
                ),
                ExpanseGroupMembers(
                    uid = "abx",
                    name = "Jai",
                    key = "xvz$60",
                    email = "jai@expanseSyncTest",
                    pic = "https://www.google.com",
                    groupId = "sdf"
                )
            ),
            split = listOf(
                TransactionSplit(
                    transactionId = "transactionID123",
                    memberKey = "xvz$23",
                    amount = 100.0,
                    paidBy = "xvz$23"
                ),
                TransactionSplit(
                    transactionId = "transactionID123",
                    memberKey = "abc$56",
                    amount = 100.0,
                    paidBy = "abc$56"
                ),
                TransactionSplit(
                    transactionId = "transactionID123",
                    memberKey = "xvz$60",
                    amount = 100.0,
                    paidBy = "abx$60"
                )
            )
        )
    }
}