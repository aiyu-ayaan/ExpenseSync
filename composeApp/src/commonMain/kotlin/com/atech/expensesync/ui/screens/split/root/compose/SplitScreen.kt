package com.atech.expensesync.ui.screens.split.root.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Camera
import androidx.compose.material.icons.twotone.GroupAdd
import androidx.compose.material.icons.twotone.Payment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.atech.expensesync.LocalDataStore
import com.atech.expensesync.component.GroupItems
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.pref.PrefKeys
import com.atech.expensesync.database.room.split.SplitGroup
import com.atech.expensesync.navigation.AppNavigation
import com.atech.expensesync.navigation.ExpanseSyncNavigation
import com.atech.expensesync.navigation.ViewExpanseBookArgs
import com.atech.expensesync.ui.screens.split.root.SplitViewModel
import com.atech.expensesync.ui.screens.split.root.compose.add_group.AddGroupScreen
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.backHandlerThreePane
import com.atech.expensesync.ui_utils.runWithDeviceCompose
import kotlinx.coroutines.flow.Flow
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


private enum class DetailScreen {
    NONE,
    ADD_EXPENSE,
    ADD_GROUP
}


@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class,
    KoinExperimentalAPI::class
)
@Composable
fun SplitScreen(
    modifier: Modifier = Modifier,
    canShowAppBar: (Boolean) -> Unit,
    navHostController: NavHostController
) {
    var detailScreen by rememberSaveable { mutableStateOf(DetailScreen.NONE) }
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    navigator.backHandlerThreePane()
    val viewModel: SplitViewModel = koinViewModel()
    val isLogIn = LocalDataStore.current.getString(PrefKeys.USER_ID).isNotBlank()
    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                canShowAppBar.invoke(true)
                MainContent(
                    modifier = Modifier,
                    groupsFlow = viewModel.splitGroups,
                    addNewGroupClick = {
                        detailScreen = DetailScreen.ADD_GROUP
                        navigator.navigateTo(
                            pane = ListDetailPaneScaffoldRole.Extra
                        )
                    },
                    linkedDeviceScreenClick = {
                        if (isLogIn) {
                            navHostController.navigate(AppNavigation.ScanScreen.route)
                        } else navHostController.navigate(
                            ExpanseSyncNavigation.LogInScreen.route
                        )
                    },
                    navHostController = navHostController
                )
            }
        },
        detailPane = {}/*when (detailScreen) {
            DetailScreen.NONE -> {
                {}
            }

            DetailScreen.ADD_EXPENSE -> {
                {}
            }

            DetailScreen.ADD_GROUP -> {
                {

                }
            }
        }*/,
        extraPane = {
            AnimatedPane {
                AddGroupScreen(
                    state = viewModel.createGroupState.value,
                    onEvent = viewModel::onAddGroupEvent,
                    onNavigateBack = {
                        navigator.navigateBack()
                        detailScreen = DetailScreen.NONE
                    }
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainContent(
    modifier: Modifier,
    groupsFlow: Flow<List<SplitGroup>>,
    addNewGroupClick: () -> Unit,
    linkedDeviceScreenClick: () -> Unit,
    navHostController: NavHostController
) {
    val itemState by groupsFlow.collectAsState(initial = emptyList())
    MainContainer(
        modifier = modifier,
        title = "Split",
        actions = {
            runWithDeviceCompose(
                onAndroid = {
                    IconButton(onClick = { linkedDeviceScreenClick.invoke() }) {
                        Icon(
                            imageVector = Icons.TwoTone.Camera,
                            contentDescription = null
                        )
                    }
                }
            )
            IconButton(onClick = { addNewGroupClick.invoke() }) {
                Icon(
                    imageVector = Icons.TwoTone.GroupAdd,
                    contentDescription = null
                )
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {
                    Text(
                        text = "Add Expense"
                    )
                },
                icon = {
                    Icon(imageVector = Icons.TwoTone.Payment, contentDescription = "Add")
                },
                onClick = { },
            )
        }
    ) { paddingValue ->
        LazyColumn(
            modifier = Modifier.padding(MaterialTheme.spacing.medium),
            contentPadding = paddingValue,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            items(itemState) { item ->
                GroupItems(
                    model = item,
                    onClick = {
                        navHostController.navigate(
                            ViewExpanseBookArgs(
                                grpId = item.groupId,
                                grpName = item.groupName
                            )
                        )
                    }
                )
            }
        }
    }
}