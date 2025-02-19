package com.atech.expensesync.ui.screens.split.add.compose.settleUpScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AddAPhoto
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.aay.compose.baseComponents.model.LegendPosition
import com.aay.compose.donutChart.DonutChart
import com.aay.compose.donutChart.model.PieChartData
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.room.split.ExpenseGroupMembers
import com.atech.expensesync.database.room.split.ExpenseTransactions
import com.atech.expensesync.database.room.split.TransactionSplit
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.generateHarmonizedColors
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDetailOfTransaction(
    modifier: Modifier = Modifier,
    transaction: ExpenseTransactions,
    groupMembers: List<ExpenseGroupMembers>,
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
                .fillMaxSize()
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
    groupMembers: List<ExpenseGroupMembers>,
    split: List<TransactionSplit>
) {
    val colorList = generateHarmonizedColors(split.size)
    val textStyle = TextStyle(
        fontSize = MaterialTheme.typography.titleSmall.fontSize,
        color = MaterialTheme.colorScheme.onSurface,
    )
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
        centerTitleStyle = textStyle,
        descriptionStyle = textStyle,
        textRatioStyle = textStyle,
        ratioLineColor = MaterialTheme.colorScheme.onSurface,
        outerCircularColor = MaterialTheme.colorScheme.onSurface,
        innerCircularColor = MaterialTheme.colorScheme.onSurface,
        legendPosition = LegendPosition.BOTTOM,
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
            transaction = ExpenseTransactions(
                transactionId = "transactionID123",
                groupId = "sdf",
                amount = 300.0,
                description = "Test",
                paidByKey = "xvz$23",
            ),
            groupMembers = listOf(
                ExpenseGroupMembers(
                    uid = "xyz",
                    name = "Ayaan",
                    key = "xvz$23",
                    email = "ayaan@expanseSyncTest",
                    pic = "https://www.google.com",
                    groupId = "sdf"
                ),
                ExpenseGroupMembers(
                    uid = "abc",
                    name = "Anshu",
                    key = "abc$56",
                    email = "anshu@expanseSyncTest",
                    pic = "https://www.google.com",
                    groupId = "sdf"
                ),
                ExpenseGroupMembers(
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