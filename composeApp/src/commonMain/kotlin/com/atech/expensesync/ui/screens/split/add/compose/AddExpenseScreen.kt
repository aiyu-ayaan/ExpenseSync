package com.atech.expensesync.ui.screens.split.add.compose

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
import androidx.compose.material.icons.twotone.AddAPhoto
import androidx.compose.material.icons.twotone.DateRange
import androidx.compose.material.icons.twotone.Description
import androidx.compose.material.icons.twotone.NoteAlt
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.atech.expensesync.component.DatePickerModal
import com.atech.expensesync.component.EditTextEnhance
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.room.split.ExpenseGroupMembers
import com.atech.expensesync.ui.screens.split.add.AddExpenseEvents
import com.atech.expensesync.ui.screens.split.add.CreateExpenseState
import com.atech.expensesync.ui.screens.split.add.ViewExpenseBookState
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.formatAmount

private enum class ExpanseBottomBar(
    val icon: ImageVector, val contentDescription: String
) {
    DATE_RANGE(Icons.TwoTone.DateRange, "date"), CAMERA_ALT(
        Icons.TwoTone.AddAPhoto, "camera"
    ),
    NOTE_ALT(Icons.TwoTone.NoteAlt, "note"),
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    modifier: Modifier = Modifier,
    viewExpenseBookState: ViewExpenseBookState,
    state: CreateExpenseState,
    groupMembers: List<ExpenseGroupMembers>,
    onNavigationClick: () -> Unit,
    onEvent: (AddExpenseEvents) -> Unit
) {

    var isDatePickerVisible by remember { mutableStateOf(false) }
    AnimatedVisibility(isDatePickerVisible) {
        DatePickerModal(onDateSelected = {
            onEvent(
                AddExpenseEvents.OnCreateExpenseStateChange(
                    state.copy(
                        date = it ?: System.currentTimeMillis()
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
            ExtendedFloatingActionButton(
                onClick = {
                    onEvent.invoke(AddExpenseEvents.AddExpenseToGroup(onComplete = onNavigationClick))
                },
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.TwoTone.Payment, contentDescription = "Add")
                    Text(
                        modifier = Modifier.padding(start = MaterialTheme.spacing.medium),
                        text = "Add"
                    )
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
                        text = "Date : ${state.formatedDate}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                    ExpanseBottomBar.entries.forEach { item ->
                        IconButton(onClick = {
                            when (item) {
                                ExpanseBottomBar.DATE_RANGE -> isDatePickerVisible =
                                    !isDatePickerVisible

                                ExpanseBottomBar.CAMERA_ALT -> {}
                                ExpanseBottomBar.NOTE_ALT -> {}
                            }
                        }) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.contentDescription
                            )
                        }
                    }
                }
            }
        }) { paddingValue ->
        Column(
            modifier = Modifier.padding(paddingValue).padding(MaterialTheme.spacing.medium),
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth(),
                text = "With you and : ${viewExpenseBookState.groupName}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            EditTextEnhance(
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Description",
                value = state.description,
                onValueChange = {
                    onEvent(AddExpenseEvents.OnCreateExpenseStateChange(state.copy(description = it)))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.TwoTone.Description, contentDescription = "Amount"
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences
                ),
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            EditTextEnhance(
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Amount",
                value = state.amount.formatAmount(),
                onValueChange = {
                    onEvent(AddExpenseEvents.OnCreateExpenseStateChange(state.copy(amount = it.takeIf { it.isNotEmpty() }
                        ?.toDoubleOrNull() ?: 0.0)))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.TwoTone.Payment, contentDescription = "Amount"
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                )
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
                    Text(state.paidBy.name)
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