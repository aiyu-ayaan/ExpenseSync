package com.atech.expensesync.ui.screens.expense.detail.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AccessTime
import androidx.compose.material.icons.twotone.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.atech.expensesync.component.AppButton
import com.atech.expensesync.component.DatePickerModal
import com.atech.expensesync.component.DropdownMenu
import com.atech.expensesync.component.EditTextEnhance
import com.atech.expensesync.component.EditTextPrice
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.component.TimePickerDialog
import com.atech.expensesync.component.setTimeToDate
import com.atech.expensesync.component.textFieldColors
import com.atech.expensesync.database.room.expense.Category
import com.atech.expensesync.database.room.expense.ExpenseBookEntry
import com.atech.expensesync.database.room.expense.PaymentMethod
import com.atech.expensesync.database.room.expense.TransactionType
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.atech.expensesync.ui.theme.appGreen
import com.atech.expensesync.ui.theme.appRed
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.utils.DatePattern
import com.atech.expensesync.utils.convertToDateFormat
import org.jetbrains.compose.ui.tooling.preview.Preview


enum class CashType {
    CASH_IN, CASH_OUT
}

@Suppress("MemberExtensionConflict")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CashInOutScreen(
    modifier: Modifier = Modifier,
    bookId: String,
    netBalance: Double,
    cashType: CashType = CashType.CASH_IN,
    onNavigationClick: () -> Unit = {},
    onSave: (ExpenseBookEntry) -> Unit = {},
) {
    val textColor = if (cashType == CashType.CASH_IN) MaterialTheme.colorScheme.appGreen
    else MaterialTheme.colorScheme.appRed

    val transactionType = if (cashType == CashType.CASH_IN) TransactionType.IN
    else TransactionType.OUT

    val dropDownMenuItem by lazy { Category.entries.toList() }
    val paymentMethod by lazy { PaymentMethod.entries.toList() }

    var amount: ExpenseBookEntry by remember {
        mutableStateOf(
            ExpenseBookEntry(
                amount = 0.0,
                transactionType = transactionType,
                bookId = bookId,
                category = Category.NONE,
                paymentMethod = PaymentMethod.CASH,
                netBalance = 0.0,
            )
        )
    }

    var currentPickDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    AnimatedVisibility(showDatePicker) {
        DatePickerModal(onDateSelected = {
            currentPickDate = it ?: System.currentTimeMillis()
        }, onDismiss = {
            showDatePicker = !showDatePicker
        })
    }
    AnimatedVisibility(showTimePicker) {
        TimePickerDialog(onConfirm = {
            currentPickDate = setTimeToDate(
                currentPickDate, it
            )
        }, onDismiss = {
            showTimePicker = !showTimePicker
        })
    }
    MainContainer(
        modifier = modifier,
        title = "Add ${if (cashType == CashType.CASH_IN) "Cash In" else "Cash Out"} Entry",
        onNavigationClick = onNavigationClick,
        titleColor = textColor,
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxWidth().padding(paddingValues)
                .padding(MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Surface(
                    modifier = Modifier.clickable {
                        showDatePicker = !showDatePicker
                    }) {
                    Row(
                        modifier = Modifier.padding(MaterialTheme.spacing.small),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                    ) {
                        Icon(
                            imageVector = Icons.TwoTone.DateRange,
                            contentDescription = "DateRange",
                        )
                        Text(
                            text = currentPickDate.convertToDateFormat(
                                DatePattern.DD_MM_YYYY_2
                            )
                        )
                    }
                }
                Surface(
                    modifier = Modifier.clickable {
                        showTimePicker = !showTimePicker
                    }) {
                    Row(
                        modifier = Modifier.padding(MaterialTheme.spacing.small),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                    ) {
                        Icon(
                            imageVector = Icons.TwoTone.AccessTime,
                            contentDescription = "Time",
                        )
                        Text(
                            text = currentPickDate.convertToDateFormat(
                                DatePattern.HH_MM_12
                            )
                        )
                    }
                }
            }

            EditTextPrice(
                modifier = Modifier.fillMaxWidth(),
                value = amount.amount.toString(),
                onValueChange = {
                    amount = amount.copy(
                        amount = it.toDouble()
                    )
                },
                placeholder = "Enter Amount *",
                colors = textFieldColors().copy(
                    focusedTextColor = textColor, unfocusedTextColor = textColor
                ),
                clearIconClick = {
                    amount = amount.copy(
                        amount = 0.0
                    )
                })
            AnimatedVisibility(
                visible = amount.amount != 0.0
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                ) {
                    EditTextEnhance(
                        modifier = Modifier.fillMaxWidth(),
                        value = "",
                        onValueChange = {

                        },
                        placeholder = "Remark",
                    )
                    DropdownMenu(
                        selectedOption = amount.category.displayName,
                        options = dropDownMenuItem.map { it.displayName },
                        placeholder = "Select Category",
                        onOptionSelected = { selectedOption ->
                            amount = amount.copy(category = dropDownMenuItem.firstOrNull {
                                it.displayName == selectedOption
                            } ?: Category.NONE)
                        },
                    )

                    DropdownMenu(
                        selectedOption = amount.paymentMethod.displayName,
                        options = paymentMethod.map { it.displayName },
                        placeholder = "Payment Type",
                        onOptionSelected = { selectedOption ->
                            amount = amount.copy(paymentMethod = paymentMethod.firstOrNull {
                                it.displayName == selectedOption
                            } ?: PaymentMethod.CASH)
                        },
                    )

                    AppButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Save",
                        onClick = {
                            onSave.invoke(
                                amount.copy(
                                    createdAt = currentPickDate,
                                    netBalance = if(netBalance==0.0)
                                        amount.amount
                                    else if (cashType == CashType.CASH_IN) {
                                        netBalance + amount.amount
                                    } else {
                                        netBalance - amount.amount
                                    }
                                )
                            )
                        }
                    )
                }
            }
        }
    }

}

@Preview
@Composable
private fun CashInOutScreenPreview() {
    ExpenseSyncTheme {
        CashInOutScreen(
            bookId = "1",
            netBalance = 0.0,
        )
    }
}