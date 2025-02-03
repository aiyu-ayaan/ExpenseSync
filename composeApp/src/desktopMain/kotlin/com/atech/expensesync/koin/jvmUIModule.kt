package com.atech.expensesync.koin

import com.atech.expensesync.ui.screens.login.LogInViewModel
import com.atech.expensesync.ui.screens.login.compose.QRHelperImp
import com.atech.expensesync.ui.screens.split.SplitViewModel
import org.koin.dsl.module

val jvmUIModule = module {
    single { SplitViewModel(get()) }
    single { LogInViewModel(get()) }
//    single { QRHelperImp() }
}