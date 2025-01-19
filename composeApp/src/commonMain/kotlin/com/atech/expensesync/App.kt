package com.atech.expensesync

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.ui.compose.login.compose.LogInScreen
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import org.jetbrains.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    ExpenseSyncTheme {
        LogInScreen()
    }
}