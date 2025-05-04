package com.atech.expensesync.ui.screens.splitv2.root.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.GroupAdd
import androidx.compose.material.icons.twotone.Payment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.models.SplitFirebase
import com.atech.expensesync.firebase.util.FirebaseResponse
import com.atech.expensesync.firebase.util.getOrNull
import com.atech.expensesync.firebase.util.isSuccess
import com.atech.expensesync.navigation.ViewSplitBookArgs
import com.atech.expensesync.ui.screens.splitv2.root.SplitV2Events
import com.atech.expensesync.ui.screens.splitv2.root.SplitViewModel
import com.atech.expensesync.ui_utils.backHandlerThreePane
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


enum class ExtraScreenType {
    ADD_EDIT, NONE
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, KoinExperimentalAPI::class)
@Composable
fun SplitV2Screen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    canShowAppBar: (Boolean) -> Unit,
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()

    val viewModel = koinViewModel<SplitViewModel>()
    val allGroups by viewModel.allSplitGroups

    com.atech.expensesync.utils.expenseSyncLogger("$allGroups")
    var extraScreenType by remember { mutableStateOf(ExtraScreenType.NONE) }

    navigator.backHandlerThreePane({
        if (extraScreenType == ExtraScreenType.ADD_EDIT) {
            viewModel.onEvent(SplitV2Events.ResetSplitState)
            extraScreenType = ExtraScreenType.NONE
        }
    })
    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                canShowAppBar.invoke(true)
                MainScreen(
                    modifier = Modifier,
                    state = allGroups,
                    addNewGroupClick = {
                        extraScreenType = ExtraScreenType.ADD_EDIT
                        navigator.navigateTo(
                            ListDetailPaneScaffoldRole.Extra
                        )
                    },
                    navHostController = navHostController

                )
            }
        },
        detailPane = {
            canShowAppBar.invoke(false)
        },
        extraPane = when (
            extraScreenType
        ) {
            ExtraScreenType.ADD_EDIT -> {
                {
                    AnimatedPane {
                        AddGroupScreen(
                            state = viewModel.addSplitState.value,
                            onEvent = viewModel::onEvent,
                            onNavigateBack = {
                                extraScreenType = ExtraScreenType.NONE
                                navigator.navigateBack()
                            }
                        )
                    }
                }
            }

            ExtraScreenType.NONE -> null
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(
    modifier: Modifier = Modifier,
    state: FirebaseResponse<List<SplitFirebase>>,
    navHostController: NavHostController,
    addNewGroupClick: () -> Unit = {},
) {
    MainContainer(
        modifier = modifier,
        title = "Split",
        actions = {
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
    ) { contentPadding ->
        if (state.isSuccess())
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = contentPadding
            ) {
                items(state.getOrNull()!!) { item ->
                    SplitGroupItem(
                        splitModel = item,
                        members = item.members,
                        onItemClick = {
                            navHostController.navigate(
                                ViewSplitBookArgs(item.groupID)
                            )
                        }
                    )
                }
            }
    }
}