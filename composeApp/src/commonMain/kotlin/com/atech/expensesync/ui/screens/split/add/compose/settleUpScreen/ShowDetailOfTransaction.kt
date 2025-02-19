package com.atech.expensesync.ui.screens.split.add.compose.settleUpScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AddAPhoto
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Description
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.icons.twotone.Money
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
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
    onAddPhotoClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onNavigationClick: () -> Unit = {},
) {
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    MainContainer(
        modifier = modifier
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        scrollBehavior = topAppBarScrollBehavior,
        title = "Details",
        onNavigationClick = onNavigationClick,
        actions = {
            IconButton(onClick = onAddPhotoClick) {
                Icon(imageVector = Icons.TwoTone.AddAPhoto, contentDescription = "Add Photo")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.TwoTone.Delete, contentDescription = "Delete Transaction")
            }
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.TwoTone.Edit, contentDescription = "Edit Transaction")
            }
        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(MaterialTheme.spacing.medium),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            item(key = "transaction_details") {
                TransactionDetailItem(
                    icon = Icons.TwoTone.Description,
                    label = "Description",
                    value = transaction.description
                )
                TransactionDetailItem(
                    icon = Icons.TwoTone.Money,
                    label = "Amount",
                    value = transaction.formatedAmount,
                    supportingText = "Paid by ${groupMembers.first { it.key == transaction.paidByKey }.name} on ${transaction.formatedDate}"
                )
            }
            item(key = "transaction_tree") {
                ShowTreeStructure(transaction, groupMembers, split)
            }
            item(key = "transaction_graph") {
                Text(
                    text = "Graphical Representation",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                CreateDonutChar(
                    groupMembers = groupMembers, split = split
                )
            }
        }
    }
}

@Composable
private fun TransactionDetailItem(
    icon: ImageVector, label: String, value: String, supportingText: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = label) },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        supportingText = supportingText?.let {
            { Text(it, style = MaterialTheme.typography.labelSmall) }
        })
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
    val pieChartData: List<PieChartData> = split.mapIndexed { index, splitTransaction ->
        val user = groupMembers.find { it.key == splitTransaction.memberKey }?.name ?: "No Name"
        PieChartData(
            partName = user, data = splitTransaction.amount, color = colorList[index]
        )
    }
    DonutChart(
        modifier = modifier.fillMaxWidth().aspectRatio(1f),
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


@Composable
private fun ShowTreeStructure(
    transaction: ExpenseTransactions,
    groupMembers: List<ExpenseGroupMembers>,
    splits: List<TransactionSplit>
) {
    val paidBy = groupMembers.first { it.key == transaction.paidByKey }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Transaction Breakdown",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TreeNode(
            text = "Paid by: ${paidBy.name}", amount = transaction.formatedAmount, isRoot = true
        )

        splits.forEach { split ->
            val member = groupMembers.first { it.key == split.memberKey }
            TreeNode(text = "${member.name} owes", amount = split.formatedAmount, isRoot = false)
        }
    }
}

@Composable
private fun TreeNode(
    text: String, amount: String, isRoot: Boolean, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(start = if (isRoot) 0.dp else 24.dp)
            .padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isRoot) {
            Box(
                modifier = Modifier.width(2.dp).height(24.dp)
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Text(
                text = "$text $amount",
                style = if (isRoot) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}


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
            ), groupMembers = listOf(
                ExpenseGroupMembers(
                    uid = "xyz",
                    name = "Ayaan",
                    key = "xvz$23",
                    email = "ayaan@expanseSyncTest",
                    pic = "https://www.google.com",
                    groupId = "sdf"
                ), ExpenseGroupMembers(
                    uid = "abc",
                    name = "Anshu",
                    key = "abc$56",
                    email = "anshu@expanseSyncTest",
                    pic = "https://www.google.com",
                    groupId = "sdf"
                ), ExpenseGroupMembers(
                    uid = "abx",
                    name = "Jai",
                    key = "xvz$60",
                    email = "jai@expanseSyncTest",
                    pic = "https://www.google.com",
                    groupId = "sdf"
                )
            ), split = listOf(
                TransactionSplit(
                    transactionId = "transactionID123",
                    memberKey = "xvz$23",
                    amount = 100.0,
                    paidBy = "xvz$23"
                ), TransactionSplit(
                    transactionId = "transactionID123",
                    memberKey = "abc$56",
                    amount = 100.0,
                    paidBy = "abc$56"
                ), TransactionSplit(
                    transactionId = "transactionID123",
                    memberKey = "xvz$60",
                    amount = 100.0,
                    paidBy = "abx$60"
                )
            )
        )
    }
}