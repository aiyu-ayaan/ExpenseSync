package com.atech.expensesync.ui.screens.expense.detail.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.atech.expensesync.component.DatePickerModal
import com.atech.expensesync.component.EditTextEnhance
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.component.TimePickerDialog
import com.atech.expensesync.component.setTimeToDate
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.atech.expensesync.ui.theme.appGreen
import com.atech.expensesync.ui.theme.appRed
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.utils.DatePattern
import com.atech.expensesync.utils.convertToDateFormat
import org.jetbrains.compose.ui.tooling.preview.Preview


enum class CashType {
    CASH_IN,
    CASH_OUT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CashInOutScreen(
    modifier: Modifier = Modifier,
    cashType: CashType = CashType.CASH_IN,
    onNavigationClick: () -> Unit = {},
) {
    val textColor = if (cashType == CashType.CASH_IN) {
        MaterialTheme.colorScheme.appGreen
    } else {
        MaterialTheme.colorScheme.appRed
    }

    var currentPickDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    MainContainer(
        modifier = modifier,
        title = "Add ${if (cashType == CashType.CASH_IN) "Cash In" else "Cash Out"} Entry",
        onNavigationClick = onNavigationClick,
        titleColor = textColor
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(paddingValues)
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
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .padding(MaterialTheme.spacing.small),
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
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .padding(MaterialTheme.spacing.small),
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

            EditTextEnhance(
                modifier = Modifier.fillMaxWidth(),
                value = "",
                onValueChange = {},
                placeholder = "Enter Amount",
            )
        }

        AnimatedVisibility(showDatePicker) {
            DatePickerModal(
                onDateSelected = {
                    currentPickDate = it ?: System.currentTimeMillis()
                },
                onDismiss = {
                    showDatePicker = !showDatePicker
                }
            )
        }
        AnimatedVisibility(showTimePicker) {
            TimePickerDialog(
                onConfirm = {
                    currentPickDate = setTimeToDate(
                        currentPickDate,
                        it
                    )
                },
                onDismiss = {
                    showTimePicker = !showTimePicker
                }
            )
        }
    }
}

@Preview
@Composable
private fun CashInOutScreenPreview() {
    ExpenseSyncTheme {
        CashInOutScreen()
    }
}