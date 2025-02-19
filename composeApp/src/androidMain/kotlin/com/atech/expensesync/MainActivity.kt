package com.atech.expensesync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.atech.expensesync.ui.theme.ExpenseSyncTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
//            val systemUiController = rememberSystemUiController()
//            systemUiController.setSystemBarsColor(
//                color = MaterialTheme.colorScheme.primary,
//            )
            ExpenseSyncTheme {
                App()
            }
        }
    }
}
