package com.atech.expensesync.ui.screens.scan.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.atech.expensesync.component.AppButton
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.backHandlerThreePane
import expensesync.composeapp.generated.resources.Res
import expensesync.composeapp.generated.resources.devices

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ScanScreen(
    modifier: Modifier = Modifier, navHostController: NavHostController
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    navigator.backHandlerThreePane()
    // Add state to control scanning
    var isScanning by remember { mutableStateOf(true) }
    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            MainContainer(
                modifier = modifier,
                title = "Linked Devices",
                onNavigationClick = {
                    navHostController.popBackStack()
                },
            ) { paddingValue ->
                Column(
                    modifier = Modifier.padding(paddingValue).padding(MaterialTheme.spacing.medium),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(.5f),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Image(
                            modifier = Modifier.fillMaxWidth().fillMaxHeight(.6f),
                            painter = org.jetbrains.compose.resources.painterResource(Res.drawable.devices),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
                        AppButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Link Device",
                            onClick = {
                                isScanning = true
                                navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                            },
                            innerPadding = MaterialTheme.spacing.small
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight()
                    ) {
                        Text(
                            text = "Devices",
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
        },
        detailPane = {
            CameraScreen(
                isScanning = isScanning,
                isScanningChanged = {
                    isScanning = it
                },
                onNavigateUpClick = {
                    navigator.navigateBack()
                },
                onLinkScanned = {
                    com.atech.expensesync.utils.expenseSyncLogger("Scanned: $it")
                }
            )
        })
}