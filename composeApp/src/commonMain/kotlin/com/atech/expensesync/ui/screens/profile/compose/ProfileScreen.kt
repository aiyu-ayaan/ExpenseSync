package com.atech.expensesync.ui.screens.profile.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AccountTree
import androidx.compose.material.icons.twotone.BugReport
import androidx.compose.material.icons.twotone.CloudSync
import androidx.compose.material.icons.twotone.Devices
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material.icons.twotone.Key
import androidx.compose.material.icons.twotone.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.models.User
import com.atech.expensesync.firebase.util.getOrNull
import com.atech.expensesync.firebase.util.isSuccess
import com.atech.expensesync.navigation.AppNavigation
import com.atech.expensesync.ui.screens.profile.ProfileViewModel
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.backHandlerThreePane
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject


enum class DetailsScreenType {
    NONE,
    CLOUD_SYNC,
    ACCOUNT,
    ACKNOWLEDGEMENTS,
    ABOUT_US,
    REPORT_BUG,
    SHARE
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    val viewModel = koinInject<ProfileViewModel>()
    val user by viewModel.user
    var screenType by remember { mutableStateOf(DetailsScreenType.NONE) }
    navigator.backHandlerThreePane()
    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            if (user.isSuccess())
                ProfileScreenCompose(
                    modifier = Modifier
                        .fillMaxWidth(),
                    user = user.getOrNull() ?: return@ListDetailPaneScaffold,
                    onLinkedDeviceClicked = {
                        navHostController.navigate(AppNavigation.ScanScreen.route)
                    },
                    onItemClick = {
                        screenType = it
                        navigator.navigateTo(
                            ListDetailPaneScaffoldRole.Detail
                        )
                    }
                )
        },
        detailPane = when (screenType) {
            DetailsScreenType.ACKNOWLEDGEMENTS -> {
                {
                   AnimatedPane {
                       AcknowledgementScreen(
                           onNavigationClick = {
                               screenType = DetailsScreenType.NONE
                               navigator.navigateBack()
                           }
                       )
                   }
                }
            }

            else -> {
                {}
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenCompose(
    modifier: Modifier = Modifier,
    user: User,
    onLinkedDeviceClicked: () -> Unit = {},
    onItemClick: (DetailsScreenType) -> Unit = {}
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val rainbowColorsBrush = remember {
        Brush.sweepGradient(
            listOf(
                Color(0xFF9575CD),
                Color(0xFFBA68C8),
                Color(0xFFE57373),
                Color(0xFFFFB74D),
                Color(0xFFFFF176),
                Color(0xFFAED581),
                Color(0xFF4DD0E1),
                Color(0xFF9575CD)
            )
        )
    }
    val borderWidth = 4.dp

    MainContainer(
        modifier = modifier.nestedScroll(
            scrollBehavior.nestedScrollConnection
        ).fillMaxSize(),
        title = "Profile",
        scrollBehavior = scrollBehavior,
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium)
            ) {
                AsyncImage(
                    model = user.photoUrl,
                    contentDescription = null,
                    modifier = Modifier.size(
                        80.dp
                    ).clip(CircleShape).border(
                        BorderStroke(borderWidth, rainbowColorsBrush),
                        CircleShape
                    ).padding(borderWidth)
                )

                Column(
                    modifier = Modifier
                        .padding(start = MaterialTheme.spacing.medium)
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        user.name,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                        user.email,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
            HorizontalDivider()
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium)
            )
            ProfileItems(
                icon = Icons.TwoTone.Key,
                title = "Account",
                description = "Manage your account settings",
                onClick = {
                    onItemClick(DetailsScreenType.ACCOUNT)
                }
            )
            com.atech.expensesync.ui_utils.runWithDeviceCompose(
                onAndroid = {
                    ProfileItems(
                        icon = Icons.TwoTone.Devices,
                        title = "Linked Devices",
                        description = "Manage your linked devices",
                        onClick = onLinkedDeviceClicked
                    )
                }
            )
            ProfileItems(
                icon = Icons.TwoTone.CloudSync,
                title = "Cloud Sync",
                description = "Manage your cloud sync settings",
                onClick = {
                    onItemClick(DetailsScreenType.CLOUD_SYNC)
                }
            )
            ProfileItems(
                icon = Icons.TwoTone.AccountTree,
                title = "Acknowledgements",
                description = "Dependencies used in this app",
                onClick = {
                    onItemClick(DetailsScreenType.ACKNOWLEDGEMENTS)
                }
            )
            ProfileItems(
                icon = Icons.TwoTone.Info,
                title = "About Us",
                description = "Learn more about us",
                onClick = {
                    onItemClick(DetailsScreenType.ABOUT_US)
                }
            )
            ProfileItems(
                icon = Icons.TwoTone.BugReport,
                title = "Report Bug",
                onClick = {
                    onItemClick(DetailsScreenType.REPORT_BUG)
                }
            )
            ProfileItems(
                icon = Icons.TwoTone.Share,
                title = "Share",
                onClick = {
                    onItemClick(DetailsScreenType.SHARE)
                }
            )

            Text(
                "Expense Sync",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(MaterialTheme.spacing.large)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun ProfileItems(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    description: String? = null,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onClick() }
            .padding(MaterialTheme.spacing.medium)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)
        ) {
            Icon(
                imageVector = icon, contentDescription = null, modifier = Modifier.size(
                    18.dp
                )
            )
            Column(
                modifier = Modifier
                    .padding(start = MaterialTheme.spacing.medium)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.bodyLarge,
                )
                if (description != null) {
                    Text(
                        description,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }
    }

}

@Preview
@Composable
private fun ProfileScreenPreview() {
    ExpenseSyncTheme {
//        ProfileScreenCompose()
    }
}