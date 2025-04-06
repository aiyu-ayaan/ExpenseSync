package com.atech.expensesync.ui.screens.app

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AttachMoney
import androidx.compose.material.icons.twotone.Build
import androidx.compose.material.icons.twotone.CalendarMonth
import androidx.compose.material.icons.twotone.Fastfood
import androidx.compose.material.icons.twotone.Payments
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.window.core.layout.WindowWidthSizeClass
import com.atech.expensesync.LocalDataStore
import com.atech.expensesync.database.pref.PrefKeys
import com.atech.expensesync.database.pref.PrefManager
import com.atech.expensesync.delegates.UploadDataDelegate
import com.atech.expensesync.firebase.usecase.MealBookUploadUseCase
import com.atech.expensesync.firebase.usecase.ObserveLogInUsingOR
import com.atech.expensesync.firebase.util.getOrNull
import com.atech.expensesync.firebase.util.isError
import com.atech.expensesync.firebase.util.isLoading
import com.atech.expensesync.firebase.util.isSuccess
import com.atech.expensesync.ui.screens.expense.root.compose.ExpenseScreen
import com.atech.expensesync.ui.screens.meal.root.compose.MealScreen
import com.atech.expensesync.ui.screens.split.root.compose.SplitScreen
import com.atech.expensesync.ui_utils.BackHandler
import com.atech.expensesync.ui_utils.SystemUiController
import com.atech.expensesync.ui_utils.lifecycler.LifeCycle
import com.atech.expensesync.ui_utils.lifecycler.LifecycleObserver
import com.atech.expensesync.ui_utils.runWithDeviceCompose
import com.atech.expensesync.usecases.UploadUseCases
import kotlinx.coroutines.CoroutineScope
import org.koin.compose.koinInject


enum class BaseAppScreen(
    val label: String, val icon: ImageVector
) {
    Split("Split", Icons.TwoTone.Payments), MessTrack(
        "Mess Track", Icons.TwoTone.Fastfood
    ),
    Expense("Expense", Icons.TwoTone.AttachMoney), Calender("History", Icons.TwoTone.CalendarMonth)
}


@Composable
fun AppScreen(
    navHostController: NavHostController
) {
    val isLogIn = LocalDataStore.current.getString(PrefKeys.USER_ID).isNotBlank()
    val desktopId = LocalDataStore.current.getString(PrefKeys.DESKTOP_USER_UID)
    val startDestination = if (isLogIn) BaseAppScreen.Split else BaseAppScreen.MessTrack
    val uploadData by UploadDataDelegate.lazy()
    var currentDestination by rememberSaveable {
        mutableStateOf(
            startDestination
        )
    }
    val mealBookUploadUseCase = koinInject<MealBookUploadUseCase>()
    val pref = koinInject<PrefManager>()
    val coroutineScope = koinInject<CoroutineScope>()
    val observeLogInUsingOR = koinInject<ObserveLogInUsingOR>()
    val uploadUseCase = koinInject<UploadUseCases>()
    val lifecycleRegistry = com.atech.expensesync.ui_utils.lifecycler.rememberLifecycleRegistry()
    uploadData.setVariables(
        mealBookUploadUseCase = mealBookUploadUseCase,
        prefManager = pref,
        scope = coroutineScope,
        uploadUseCase = uploadUseCase
    )

    DisposableEffect(lifecycleRegistry) {
        val observer = object : LifecycleObserver {
            override fun onStateChanged(state: LifeCycle) {
                when (state) {
                    LifeCycle.ON_STOP -> {
                        uploadData.uploadMealData()
                    }

                    else -> {
                        com.atech.expensesync.utils.expenseSyncLogger(
                            "State Details: $state",
                        )
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
            BaseAppScreen.entries.filter {
                if (!isLogIn) it != BaseAppScreen.Split else true
            }.forEach { item ->
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

                BaseAppScreen.Calender -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            modifier = Modifier.fillMaxWidth().fillMaxHeight(.5f),
                            imageVector = Icons.TwoTone.Build,
                            contentDescription = "Budget"
                        )
                        Text(
                            "Calender", style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            "Work in Progress", style = MaterialTheme.typography.bodySmall
                        )
                    }
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