package com.atech.expensesync.ui.compose.split.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.ui.theme.spacing


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SplitScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            MainContent(
                modifier = Modifier
            )
        },
        detailPane = {

        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainContent(modifier: Modifier) {
    MainContainer(
        modifier = modifier,
        title = "Split",
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
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
    }
}