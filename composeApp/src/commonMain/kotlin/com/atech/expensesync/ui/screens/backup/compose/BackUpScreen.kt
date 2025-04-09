package com.atech.expensesync.ui.screens.backup.compose

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
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
import expensesync.composeapp.generated.resources.Res
import expensesync.composeapp.generated.resources.img_download_desktop
import expensesync.composeapp.generated.resources.img_download_mobile
import org.jetbrains.compose.resources.painterResource
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

    val infiniteTransition = rememberInfiniteTransition(label = "backup-animation")
    val dotCount by infiniteTransition.animateValue(
        initialValue = 0,
        targetValue = 4,
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "dots-animation"
    )
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
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(contentPadding)
                .padding(MaterialTheme.spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            com.atech.expensesync.ui_utils.runWithDeviceCompose(
                onDesktop = {
                    Image(
                        painter = painterResource(Res.drawable.img_download_desktop),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(MaterialTheme.spacing.medium)
                            .fillMaxWidth()
                            .fillMaxHeight(.5f),
                    )
                },

                onAndroid = {
                    Image(
                        painter = painterResource(Res.drawable.img_download_mobile),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(.4f),
                    )
                }
            )
            val dots = ".".repeat(dotCount % 4)
            Text(
                text = "Back Up is in progress$dots",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(top = MaterialTheme.spacing.medium),
            )

            // Indeterminate progress bar
            LinearProgressIndicator(
                modifier = Modifier
                    .padding(top = MaterialTheme.spacing.medium)
                    .fillMaxWidth(0.7f),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
}