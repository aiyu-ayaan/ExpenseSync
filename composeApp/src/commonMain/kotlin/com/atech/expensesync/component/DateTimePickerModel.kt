package com.atech.expensesync.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.datetime.TimeZone
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(onDismissRequest = onDismiss, confirmButton = {
        TextButton(onClick = {
            val selectedDateMillis = datePickerState.selectedDateMillis
            val dateWithCurrentTime = selectedDateMillis?.let { getDateWithCurrentTime(it) }

            onDateSelected(dateWithCurrentTime)
            onDismiss()
        }) {
            Text("OK")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    }) {
        DatePicker(state = datePickerState)
    }
}

data class Time(
    val hour: Int,
    val minute: Int,
    val second: Int,
    val millisecond: Int
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onConfirm: (time: Time) -> Unit,
    onDismiss: () -> Unit
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false,
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Time") },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentAlignment = Alignment.Center
            ) {
                TimePicker(
                    state = timePickerState,
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(
                        Time(
                            hour = timePickerState.hour,
                            minute = timePickerState.minute,
                            second = currentTime.get(Calendar.SECOND),
                            millisecond = currentTime.get(Calendar.MILLISECOND)
                        )
                    )
                    onDismiss()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


fun getDateWithCurrentTime(dateMillis: Long): Long {
    val timeZone = TimeZone.currentSystemDefault().id
    val selectedDate = Calendar.getInstance(java.util.TimeZone.getTimeZone(timeZone)).apply {
        timeInMillis = dateMillis
    }

    val currentTime = Calendar.getInstance(java.util.TimeZone.getTimeZone(timeZone))

    selectedDate.set(Calendar.HOUR_OF_DAY, currentTime.get(Calendar.HOUR_OF_DAY))
    selectedDate.set(Calendar.MINUTE, currentTime.get(Calendar.MINUTE))
    selectedDate.set(Calendar.SECOND, currentTime.get(Calendar.SECOND))
    selectedDate.set(Calendar.MILLISECOND, currentTime.get(Calendar.MILLISECOND))

    return selectedDate.timeInMillis
}

fun setTimeToDate(dateMillis: Long, time: Time): Long {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = dateMillis
        set(Calendar.HOUR_OF_DAY, time.hour)
        set(Calendar.MINUTE, time.minute)
        set(Calendar.SECOND, time.second)
        set(Calendar.MILLISECOND, time.millisecond)
    }
    return calendar.timeInMillis
}

