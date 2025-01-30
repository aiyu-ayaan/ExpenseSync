package com.atech.expensesync.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    text: String,
    enable: Boolean = true,
    innerPadding: Dp = 0.dp,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = colors,
        enabled = enable
    ) {
        Text(
            text,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun ButtonWithBorder(
    modifier: Modifier = Modifier,
    text: String,
    enable: Boolean = true,
    innerPadding: Dp = 0.dp,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    onClick: () -> Unit = {}
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        colors = colors,
        enabled = enable
    ) {
        Text(
            text,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
