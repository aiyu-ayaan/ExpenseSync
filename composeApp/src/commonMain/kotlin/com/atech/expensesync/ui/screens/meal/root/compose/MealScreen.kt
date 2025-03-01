package com.atech.expensesync.ui.screens.meal.root.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Book
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
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
import com.atech.expensesync.ui.screens.meal.root.AddMealBookState
import com.atech.expensesync.ui.screens.meal.root.MealScreenEvents
import com.atech.expensesync.ui.screens.meal.root.MealViewModel
import com.atech.expensesync.ui.screens.meal.root.compose.add.AddMealBookScreen
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.atech.expensesync.ui_utils.backHandlerThreePane
import com.atech.expensesync.ui_utils.koinViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

//private enum class DetailsScreenType {
//    AddMealBook, None
//}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MealScreen(
    modifier: Modifier = Modifier,
    canShowAppBar: (Boolean) -> Unit,
    navHostController: NavHostController
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>()
    val viewModel = koinViewModel<MealViewModel>()
//    var detailsScreenType by remember { mutableStateOf(DetailsScreenType.None) }

    navigator.backHandlerThreePane(backAction = {
//        detailsScreenType = DetailsScreenType.None
        viewModel.onEvent(MealScreenEvents.OnMealScreenStateChange(null))
    })
    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                canShowAppBar.invoke(true)
                MealListScreen(
                    onAddMealBookClick = {
                        viewModel.onEvent(MealScreenEvents.OnMealScreenStateChange(AddMealBookState()))
//                        detailsScreenType = DetailsScreenType.AddMealBook
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Extra)
                    })
            }
        },
        extraPane = {
            AnimatedPane {
                canShowAppBar.invoke(false)
                AddMealBookScreen(
                    state = viewModel.addMealState.value ?: return@AnimatedPane,
                    onEvent = viewModel::onEvent,
                    onNavigationClick = {
                        viewModel.onEvent(MealScreenEvents.OnMealScreenStateChange(null))
                        navigator.navigateBack()
//                        detailsScreenType = DetailsScreenType.None
                    })
            }
        },
        detailPane = {}/* when (detailsScreenType) {
            DetailsScreenType.AddMealBook -> {
                {

                }
            }

            DetailsScreenType.None -> {
                {}
            }
        }*/
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MealListScreen(
    modifier: Modifier = Modifier, onAddMealBookClick: () -> Unit = {}
) {
    MainContainer(
        modifier = modifier, title = "Meal", floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Meal") }, icon = {
                    Icon(Icons.TwoTone.Book, contentDescription = "Add Meal")
                }, onClick = onAddMealBookClick
            )
        }) { paddingValues ->

    }
}

@Preview
@Composable
private fun MealScreenPreview() {
    ExpenseSyncTheme {
        MealListScreen(
        )
    }
}