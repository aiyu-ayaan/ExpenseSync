package com.atech.expensesync.koin

import com.atech.expensesync.ui.compose.login.LogInViewModel
import com.atech.expensesync.ui.compose.split.SplitViewModel
import org.koin.dsl.module

val jvmUIModule = module {
    single { SplitViewModel(get()) }
    single { LogInViewModel(get()) }
}