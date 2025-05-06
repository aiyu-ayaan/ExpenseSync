package com.atech.expensesync.ui.screens.splitv2.details.compose.settleUp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Payment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.atech.expensesync.component.AppButton
import com.atech.expensesync.component.EditTextPrice
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.models.GroupMember
import com.atech.expensesync.database.models.SplitTransaction
import com.atech.expensesync.database.models.SplitTransactionElement
import com.atech.expensesync.ui.screens.splitv2.details.SplitDetailsEvents
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.utils.LoggerType

@Suppress("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettleMoneyScreen(
    modifier: Modifier = Modifier,
    members: List<GroupMember> = emptyList(),
    triple: Triple<String, String, Double> = Triple("", "", 0.0),
    onNavigateBack: () -> Unit = {},
    onEvent: (SplitDetailsEvents) -> Unit = {}
) {
    val (debtorUid, creditorUid, amount) = triple

    MainContainer(
        modifier = modifier, title = "SettleUp", onNavigationClick = onNavigateBack
    ) { paddingValues ->
        var amount by remember { mutableDoubleStateOf(amount) }
        Column(
            modifier = Modifier.padding(MaterialTheme.spacing.medium).padding(paddingValues)
        ) {
            EditTextPrice(
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Amount",
                value = String.format(
                    "%.2f", amount
                ),
                onValueChange = {
                    amount = it.takeIf { it.isNotEmpty() }?.toDoubleOrNull() ?: 0.0
                },
                clearIconClick = {
                    amount = 0.0
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.TwoTone.Payment, contentDescription = "Amount"
                    )
                })
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
            AppButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Settle Up",
                innerPadding = MaterialTheme.spacing.small,
                shape = RoundedCornerShape(10),
                onClick = {
                    onEvent(
                        SplitDetailsEvents.SettleUpClick(
                            SplitTransaction(
                                amount = amount * 2,
                                settled = true,
                                createdByUid = debtorUid,
                                splitMembers = listOf(
                                    SplitTransactionElement(
                                        GroupMember(
                                            uid = debtorUid,
                                            name = members.find { it.uid == debtorUid }?.name ?: "",
                                            pic = members.find { it.uid == debtorUid }?.pic ?: ""
                                        ),
                                        amount = amount
                                    ),
                                    SplitTransactionElement(
                                        GroupMember(
                                            uid = creditorUid,
                                            name = members.find { it.uid == creditorUid }?.name
                                                ?: "",
                                            pic = members.find { it.uid == creditorUid }?.pic
                                                ?: ""
                                        ),
                                        amount = amount
                                    )
                                )
                            )
                        ) { exception ->
                            if (exception != null) {
                                com.atech.expensesync.utils.expenseSyncLogger(
                                    exception.message.toString(),
                                    LoggerType.ERROR
                                )
                                return@SettleUpClick
                            }
                            com.atech.expensesync.ui_utils.showToast(
                                "Added Successfully !!"
                            )
                            onNavigateBack.invoke()
                        }
                    )
                })
        }
    }
}