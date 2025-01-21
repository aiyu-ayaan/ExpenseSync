package com.atech.expensesync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

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
