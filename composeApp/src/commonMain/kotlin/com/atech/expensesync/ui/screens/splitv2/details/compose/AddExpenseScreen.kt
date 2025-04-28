package com.atech.expensesync.ui.screens.splitv2.details.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.DateRange
import androidx.compose.material.icons.twotone.Description
import androidx.compose.material.icons.twotone.Payment
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.atech.expensesync.LocalDataStore
import com.atech.expensesync.component.DatePickerModal
import com.atech.expensesync.component.EditTextEnhance
import com.atech.expensesync.component.EditTextPrice
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.pref.PrefKeys
import com.atech.expensesync.database.room.splitv2.GroupMembers
import com.atech.expensesync.database.room.splitv2.SplitModel
import com.atech.expensesync.database.room.splitv2.SplitTransactions
import com.atech.expensesync.ui.screens.splitv2.details.SplitDetailsEvents
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.utils.convertToDateFormat


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    modifier: Modifier = Modifier,
    state: SplitTransactions,
    splitModel: SplitModel,
    groupMembers: List<GroupMembers>,
    onEvent: (SplitDetailsEvents) -> Unit,
    onNavigationClick: () -> Unit,
) {

    val pref = LocalDataStore.current
    var isDatePickerVisible by remember { mutableStateOf(false) }

    AnimatedVisibility(isDatePickerVisible) {
        DatePickerModal(onDateSelected = {
            onEvent(
                SplitDetailsEvents.OnEditTransaction(
                    state.copy(
                        createdAt = it ?: System.currentTimeMillis()
                    )
                )
            )
        }, onDismiss = { isDatePickerVisible = false })
    }
    MainContainer(
        modifier = modifier,
        title = "Add Expense",
        onNavigationClick = onNavigationClick,
        floatingActionButton = {
            AnimatedVisibility(state.amount != 0.0) {
                ExtendedFloatingActionButton(
                    onClick = {
                        onEvent.invoke(SplitDetailsEvents.OnEditTransaction(state.copy(
                            paidByUid = pref.getString(PrefKeys.USER_ID) // TODO: replace with selected user
                        )))
                        onEvent.invoke(SplitDetailsEvents.SaveTransaction)
                        onNavigationClick.invoke()
                    },
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.TwoTone.Payment, contentDescription = "Add")
                        Text(
                            modifier = Modifier.padding(start = MaterialTheme.spacing.medium),
                            text = "Add Expense"
                        )
                    }
                }
            }
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Date : ${state.createdAt.convertToDateFormat()}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

                    IconButton(onClick = {
                        isDatePickerVisible = !isDatePickerVisible
                    }) {
                        Icon(
                            imageVector = Icons.TwoTone.DateRange,
                            contentDescription = "Date"
                        )
                    }

                }
            }
        }) { paddingValue ->
        Column(
            modifier = Modifier.padding(paddingValue).padding(MaterialTheme.spacing.medium),
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth(),
                text = "With you and : ${splitModel.groupName}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            EditTextEnhance(
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Description",
                value = state.description,
                onValueChange = {
                    onEvent(SplitDetailsEvents.OnEditTransaction(state.copy(description = it)))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.TwoTone.Description, contentDescription = "Amount"
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            EditTextPrice(
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Amount",
                value = state.amount.toString(),
                onValueChange = {
                    onEvent(SplitDetailsEvents.OnEditTransaction(state.copy(amount = it.takeIf { it.isNotEmpty() }
                        ?.toDoubleOrNull() ?: 0.0)))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.TwoTone.Payment, contentDescription = "Amount"
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                ),
                clearIconClick = {
                    onEvent(SplitDetailsEvents.OnEditTransaction(state.copy(amount = 0.0)))
                }
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically

            ) {
                Text("Paid By")
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                TextButton(
                    onClick = {}, border = BorderStroke(
                        1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                    )
                ) {
                    Text(groupMembers.firstOrNull {
                        it.uid == state.paidByUid
                    }?.name ?: groupMembers.firstOrNull {
                        it.uid == pref.getString(PrefKeys.USER_ID)
                    }?.name ?: "Select User")
                }
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                Text("and split")
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                TextButton(
                    onClick = {}, border = BorderStroke(
                        1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                    )
                ) {
                    Text(state.splitType.name)
                }
            }
        }
    }
}