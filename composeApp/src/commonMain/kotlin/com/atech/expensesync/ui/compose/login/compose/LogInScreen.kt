package com.atech.expensesync.ui.compose.login.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.atech.expensesync.LocalDataStore
import com.atech.expensesync.component.AppButton
import com.atech.expensesync.component.ButtonWithBorder
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.pref.PrefKeys
import com.atech.expensesync.navigation.ExpanseSyncRoutes
import com.atech.expensesync.ui.theme.spacing
import expensesync.composeapp.generated.resources.Res
import expensesync.composeapp.generated.resources.login
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    val pref = LocalDataStore.current
    MainContainer(
        modifier = modifier,
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = MaterialTheme.spacing.large),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ButtonWithBorder(
                    modifier = Modifier.fillMaxWidth(.3f)
                        .padding(MaterialTheme.spacing.medium),
                    innerPadding = MaterialTheme.spacing.small,
                    text = "Skip",
                    onClick = {
                        navHostController.navigate(ExpanseSyncRoutes.AppScreens.route) {
//                            pref.saveBoolean(
//                                PrefKeys.IS_LOG_IN_SKIP,
//                                true
//                            )
                            launchSingleTop = true
                            popUpTo(ExpanseSyncRoutes.LOGIN.route) {
                                inclusive = true
                            }
                        }
                    }
                )
                AppButton(
                    modifier = Modifier.fillMaxWidth()
                        .padding(MaterialTheme.spacing.medium),
                    innerPadding = MaterialTheme.spacing.small,
                    text = "Sign Up", onClick = {}
                )
            }
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(contentPadding),
        ) {
            Image(
                modifier = Modifier.fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .padding(
                        horizontal = MaterialTheme.spacing.large
                    ),
                painter = painterResource(Res.drawable.login),
                contentDescription = "Money",
            )
            Column(
                modifier = Modifier.fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .padding(MaterialTheme.spacing.medium),
                verticalArrangement = Arrangement.Bottom,
            ) {
                Text(
                    text = "Set Your Budget With Expense Sync",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
                Text(
                    text = "Set your budget and track your expenses in one place",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}