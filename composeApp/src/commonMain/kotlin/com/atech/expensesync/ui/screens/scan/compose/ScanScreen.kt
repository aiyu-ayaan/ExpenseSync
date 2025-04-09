package com.atech.expensesync.ui.screens.scan.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Devices
import androidx.compose.material.icons.twotone.RemoveCircleOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
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
import com.atech.expensesync.database.models.DesktopLogInDetails
import com.atech.expensesync.firebase.util.FirebaseResponse
import com.atech.expensesync.firebase.util.getOrNull
import com.atech.expensesync.firebase.util.isError
import com.atech.expensesync.firebase.util.isLoading
import com.atech.expensesync.firebase.util.isSuccess
import com.atech.expensesync.ui.screens.scan.ScanEvents
import com.atech.expensesync.ui.screens.scan.ScanViewModel
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.backHandlerThreePane
import com.atech.expensesync.utils.convertToDateFormat
import com.atech.expensesync.utils.expenseSyncLogger
import expensesync.composeapp.generated.resources.Res
import expensesync.composeapp.generated.resources.devices
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3AdaptiveApi::class,
    KoinExperimentalAPI::class
)
@Composable
fun ScanScreen(
    modifier: Modifier = Modifier, navHostController: NavHostController
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    val viewModel = koinViewModel<ScanViewModel>()
    navigator.backHandlerThreePane()
    // Add state to control scanning
    var isScanning by remember { mutableStateOf(true) }
    var isDialogVisible by remember { mutableStateOf(false) }
    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                MainContainer(
                    modifier = modifier,
                    title = "Linked Devices",
                    onNavigationClick = {
                        navHostController.popBackStack()
                    },
                ) { paddingValue ->
                    AnimatedVisibility(isDialogVisible) {
                        AlertDialog(
                            onDismissRequest = {
                                isDialogVisible = false
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.TwoTone.RemoveCircleOutline,
                                    contentDescription = "Delete Meal"
                                )
                            },
                            title = {
                                Text("Remove Device")
                            },
                            text = {
                                Text("Are you sure you want to log out from this device?")
                            },
                            confirmButton = {
                                TextButton(onClick = {
                                    isDialogVisible = false
                                    viewModel.onEvent(
                                        ScanEvents.PerformLogOut
                                    )
                                }) {
                                    Text("Delete")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = {
                                    isDialogVisible = !isDialogVisible
                                }) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }
                    Column(
                        modifier = Modifier.padding(paddingValue)
                            .padding(MaterialTheme.spacing.medium),
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
                        val logInDetails by viewModel.logInDetails
                        Column(
                            modifier = Modifier.fillMaxWidth().fillMaxHeight()
                        ) {
                            if (logInDetails.isError() || logInDetails.isLoading()) {
                                Text(
                                    text = "Devices",
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                            if (logInDetails.isSuccess()) {
                                val data = logInDetails.getOrNull() ?: return@Column
                                ListItem(
                                    headlineContent = {
                                        Text(data.systemName)
                                    },
                                    leadingContent = {
                                        Icon(
                                            imageVector = Icons.TwoTone.Devices,
                                            contentDescription = null,
                                        )
                                    },
                                    supportingContent = {
                                        Text(
                                            text = "Logged in at ${data.longInAt.convertToDateFormat()}",
                                        )
                                    },
                                    trailingContent = {
                                        IconButton(onClick = {
                                            isDialogVisible = !isDialogVisible
                                        }) {
                                            Icon(
                                                imageVector = Icons.TwoTone.RemoveCircleOutline,
                                                contentDescription = null
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        detailPane = {
            AnimatedPane {
                CameraScreen(isScanning = isScanning, isScanningChanged = {
                    isScanning = it
                }, onNavigateUpClick = {
                    navigator.navigateBack()
                }, onLinkScanned = { data ->
                    viewModel.onEvent(ScanEvents.OnScanSuccess(data) { state ->
                        when (state) {
                            is FirebaseResponse.Error -> {
                                expenseSyncLogger(
                                    "Error: ${state.error}"
                                )
                            }

                            FirebaseResponse.Loading -> {}
                            is FirebaseResponse.Success<DesktopLogInDetails> -> {
                                expenseSyncLogger(
                                    "${state.data.systemUid} ${state.data.systemName}"
                                )
                            }

                            FirebaseResponse.Empty -> {
                                expenseSyncLogger(
                                    "Empty"
                                )
                            }
                        }
                    })
                })
            }
        })
}