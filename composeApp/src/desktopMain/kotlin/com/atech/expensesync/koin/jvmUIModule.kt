package com.atech.expensesync.koin

import com.atech.expensesync.ui.screens.login.LogInViewModel
import com.atech.expensesync.ui.screens.split.add.AddExpanseViewModel
import com.atech.expensesync.ui.screens.split.root.SplitViewModel
import org.koin.dsl.module

val jvmUIModule = module {
    single { SplitViewModel(get(), get()) }
    single { LogInViewModel(get(), get()) }
    single { AddExpanseViewModel(get()) }
//    single { QRHelperImp() }
}