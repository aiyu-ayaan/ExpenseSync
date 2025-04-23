package com.atech.expensesync.ui.screens.app

import androidx.compose.animation.AnimatedContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AccountCircle
import androidx.compose.material.icons.twotone.AttachMoney
import androidx.compose.material.icons.twotone.Fastfood
import androidx.compose.material.icons.twotone.Payments
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.window.core.layout.WindowWidthSizeClass
import com.atech.expensesync.LocalDataStore
import com.atech.expensesync.LocalUploadDataHelper
import com.atech.expensesync.database.pref.PrefKeys
import com.atech.expensesync.database.room.MaintenanceDao
import com.atech.expensesync.firebase.usecase.ObserveLogInUsingOR
import com.atech.expensesync.firebase.util.getOrNull
import com.atech.expensesync.firebase.util.isError
import com.atech.expensesync.firebase.util.isLoading
import com.atech.expensesync.firebase.util.isSuccess
import com.atech.expensesync.ui.screens.expense.root.compose.ExpenseScreen
import com.atech.expensesync.ui.screens.meal.root.compose.MealScreen
import com.atech.expensesync.ui.screens.profile.compose.ProfileScreen
import com.atech.expensesync.ui.screens.split.root.compose.SplitScreen
import com.atech.expensesync.ui_utils.BackHandler
import com.atech.expensesync.ui_utils.SystemUiController
import com.atech.expensesync.ui_utils.lifecycler.LifeCycle
import com.atech.expensesync.ui_utils.lifecycler.LifecycleObserver
import com.atech.expensesync.ui_utils.runWithDeviceCompose
import org.koin.compose.koinInject


enum class BaseAppScreen(
    val label: String, val icon: ImageVector
) {
    Split("Split", Icons.TwoTone.Payments), MessTrack(
        "Mess Track", Icons.TwoTone.Fastfood
    ),
    Expense("Expense", Icons.TwoTone.AttachMoney), Profile("Profile", Icons.TwoTone.AccountCircle)
}


@Composable
fun AppScreen(
    navHostController: NavHostController
) {
    val desktopId = LocalDataStore.current.getString(PrefKeys.DESKTOP_USER_UID)
    val startDestination = BaseAppScreen.Split
    val uploadDataHelper = LocalUploadDataHelper.current
    var currentDestination by rememberSaveable {
        mutableStateOf(
            startDestination
        )
    }
    val pref = uploadDataHelper.prefManager
    val observeLogInUsingOR = koinInject<ObserveLogInUsingOR>()
    val lifecycleRegistry = com.atech.expensesync.ui_utils.lifecycler.rememberLifecycleRegistry()
    val maintenanceDao: MaintenanceDao = koinInject()


    DisposableEffect(lifecycleRegistry) {
        val observer = object : LifecycleObserver {
            override fun onStateChanged(state: LifeCycle) {
                when (state) {
                    LifeCycle.ON_STOP -> {
                        uploadDataHelper.uploadMealData()
                    }

                    else -> {
                        // Do nothing
                    }
                }
            }
        }

        lifecycleRegistry.addObserver(observer)

        onDispose {
            lifecycleRegistry.removeObserver(observer)
        }
    }

    runWithDeviceCompose(
        onDesktop = {
            LaunchedEffect(true) {
                observeLogInUsingOR.invoke(desktopId).collect {
                    if (it.isLoading()) return@collect
                    if (it.isError()) return@collect
                    if (it.isSuccess()) {
                        if (it.getOrNull() != null && it.getOrNull()!!.systemUid != desktopId) {
                            pref.clearAll()
                            maintenanceDao.deleteAll()
                            com.atech.expensesync.utils.restartApp()
                        }
                    }
                }
            }
        })

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
    BackHandler(currentDestination != startDestination) {
        currentDestination = startDestination
    }
    SystemUiController(
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
                    item = item, selected = item == currentDestination, onClick = {
                        currentDestination = item
                    })
            }
        },
    ) {
        AnimatedContent(
            targetState = currentDestination,
        ) { destination ->
            when (destination) {
                BaseAppScreen.Split -> {
                    SplitScreen(
                        navHostController = navHostController, canShowAppBar = {
                            showNavigation = it
                        })
                }

                BaseAppScreen.Expense -> {
                    ExpenseScreen(
                        navHostController = navHostController, canShowAppBar = {
                            showNavigation = it
                        })
                }

                BaseAppScreen.MessTrack -> MealScreen(
                    navHostController = navHostController, canShowAppBar = {
                        showNavigation = it
                    })

                BaseAppScreen.Profile -> {
                    ProfileScreen(
                        navHostController = navHostController
                    )
                }
            }
        }
    }
}

fun NavigationSuiteScope.navItemEntry(
    modifier: Modifier = Modifier, item: BaseAppScreen, selected: Boolean, onClick: () -> Unit
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