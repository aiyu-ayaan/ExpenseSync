package com.atech.expensesync.ui.compose.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AttachMoney
import androidx.compose.material.icons.twotone.CalendarMonth
import androidx.compose.material.icons.twotone.Fastfood
import androidx.compose.material.icons.twotone.Splitscreen
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.window.core.layout.WindowWidthSizeClass


enum class BaseAppScreen(
    val label: String, val icon: ImageVector
) {
    Split("Split", Icons.TwoTone.Splitscreen),
    Budget("Budget", Icons.TwoTone.AttachMoney),
    Settings("Mess Track", Icons.TwoTone.Fastfood),
    Calender("History", Icons.TwoTone.CalendarMonth)
}


@Composable
fun AppScreen(
    navHostController: NavHostController
) {
    var currentDestination by rememberSaveable { mutableStateOf(BaseAppScreen.Split) }
    val adaptiveInfo = currentWindowAdaptiveInfo()
    var showNavigation by remember { mutableStateOf(true) }
    val customNavSuiteType = with(adaptiveInfo) {
        when (windowSizeClass.windowWidthSizeClass) {
            WindowWidthSizeClass.COMPACT -> when {
                showNavigation -> NavigationSuiteType.NavigationBar
                else -> NavigationSuiteType.None
            }

            WindowWidthSizeClass.MEDIUM -> NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
                adaptiveInfo
            )

            WindowWidthSizeClass.EXPANDED -> NavigationSuiteType.NavigationRail
            else -> NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
        }
    }
    com.atech.expensesync.utils.systemUiController(
        bottomNavigationBarColor = BottomAppBarDefaults.containerColor,
        statusBarColor = BottomAppBarDefaults.containerColor,
    )
    NavigationSuiteScaffold(
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationRailContainerColor = BottomAppBarDefaults.containerColor
        ),
        layoutType = customNavSuiteType,
        navigationSuiteItems = {
            BaseAppScreen.entries.forEach { item ->
                navItemEntry(
                    item = item,
                    selected = item == currentDestination,
                    onClick = {
                        currentDestination = item
                    }
                )
            }
        },
    ) {
        when (currentDestination) {
            BaseAppScreen.Split -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Split")
                }
            }

            BaseAppScreen.Budget -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Budget")
                }
            }

            BaseAppScreen.Settings -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Settings")
                }
            }

            BaseAppScreen.Calender -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Calender")
                }
            }
        }
    }
}

fun NavigationSuiteScope.navItemEntry(
    modifier: Modifier = Modifier,
    item: BaseAppScreen,
    selected: Boolean,
    onClick: () -> Unit
) {
    item(
        modifier = modifier, selected = selected, onClick = onClick, label = {
//        Text(item.label)
        }, icon = {
            Icon(
                imageVector = item.icon, contentDescription = item.label
            )
        }, alwaysShowLabel = false
    )
}