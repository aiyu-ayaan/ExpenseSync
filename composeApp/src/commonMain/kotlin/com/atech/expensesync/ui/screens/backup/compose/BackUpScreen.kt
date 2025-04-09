package com.atech.expensesync.ui.screens.backup.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Backup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.atech.expensesync.LocalDataStore
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.pref.PrefKeys
import com.atech.expensesync.navigation.AppNavigation
import com.atech.expensesync.navigation.LogInNavigation
import com.atech.expensesync.ui.screens.backup.BackUpScreenEvents
import com.atech.expensesync.ui.screens.backup.BackUpViewModel
import com.atech.expensesync.ui.theme.spacing
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackUpScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    val viewModel = koinInject<BackUpViewModel>()
    val isBackUpDone by viewModel.isBackUpDone
    val pref = LocalDataStore.current
    LaunchedEffect(isBackUpDone) {
        if (isBackUpDone) {
            pref.saveBoolean(PrefKeys.IS_BACKUP_DONE, true)
            navHostController.navigate(AppNavigation.AppScreen.route) {
                launchSingleTop = true
                popUpTo(LogInNavigation.BackUpScreen.route) {
                    inclusive = true
                }
            }
        }
    }
    MainContainer(
        modifier = modifier
    ) { contentPadding ->
        viewModel.onEvent(BackUpScreenEvents.OnMealDataBackUpDone)
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(contentPadding)
                .padding(MaterialTheme.spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.TwoTone.Backup,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.5f),
            )
            Text(
                text = "Back Up is in progress....",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(top = MaterialTheme.spacing.medium),
            )
        }
    }
}