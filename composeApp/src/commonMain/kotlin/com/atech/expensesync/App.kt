package com.atech.expensesync

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.atech.expensesync.database.pref.PrefManager
import com.atech.expensesync.navigation.ExpanseSyncNavigation
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

val LocalDataStore = staticCompositionLocalOf<PrefManager> { error("No DataStore provided") }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App(
    pref: PrefManager,
    navHostController: NavHostController = rememberNavController()
) {
    CompositionLocalProvider {
        LocalDataStore provides pref
        ExpenseSyncTheme {
            Scaffold { contentPadding ->
                ExpanseSyncNavigation(
                    modifier = Modifier.padding(contentPadding),
                    navHostController = navHostController
                )
            }
        }
    }
}