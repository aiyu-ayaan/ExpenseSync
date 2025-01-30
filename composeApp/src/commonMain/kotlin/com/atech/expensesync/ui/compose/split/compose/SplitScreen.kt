package com.atech.expensesync.ui.compose.split.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.atech.expensesync.component.GroupItems
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.room.split.SplitGroup
import com.atech.expensesync.ui.compose.split.SplitViewModel
import com.atech.expensesync.ui.compose.split.compose.add_group.AddGroupScreen
import com.atech.expensesync.ui.theme.spacing
import com.atech.expensesync.ui_utils.koinViewModel
import kotlinx.coroutines.flow.Flow


private enum class DetailScreen {
    NONE,
    ADD_EXPENSE,
    ADD_GROUP
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SplitScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    var detailScreen by rememberSaveable { mutableStateOf(DetailScreen.NONE) }
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    val viewModel: SplitViewModel = koinViewModel()
    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            MainContent(
                modifier = Modifier,
                share = viewModel.splitGroups,
                addNewGroupClick = {
                    detailScreen = DetailScreen.ADD_GROUP
                    navigator.navigateTo(
                        pane = ListDetailPaneScaffoldRole.Detail
                    )
                }
            )
        },
        detailPane = when (detailScreen) {
            DetailScreen.NONE -> {
                {}
            }

            DetailScreen.ADD_EXPENSE -> {
                {}
            }

            DetailScreen.ADD_GROUP -> {
                {
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
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainContent(
    modifier: Modifier,
    share: Flow<List<SplitGroup>>,
    addNewGroupClick: () -> Unit
) {
    val itemState by share.collectAsState(initial = emptyList())
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
                onClick = { },
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.TwoTone.Payment, contentDescription = "Add")
                    Text(
                        modifier = Modifier.padding(start = MaterialTheme.spacing.medium),
                        text = "Add Expense"
                    )
                }
            }
        }
    ) { paddingValue ->
        LazyColumn(
            modifier = Modifier.padding(MaterialTheme.spacing.medium),
            contentPadding = paddingValue
        ) {
            items(itemState) { item ->
                GroupItems(
                    model = item
                )
            }
        }
    }
}