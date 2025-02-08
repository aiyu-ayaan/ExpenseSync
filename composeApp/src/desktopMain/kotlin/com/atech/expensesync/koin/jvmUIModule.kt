package com.atech.expensesync.koin

import com.atech.expensesync.ui.screens.login.LogInViewModel
import com.atech.expensesync.ui.screens.split.add.AddExpenseViewModel
import com.atech.expensesync.ui.screens.split.root.SplitViewModel
import org.koin.dsl.module

val jvmUIModule = module {
    single { SplitViewModel(get(), get()) }
    single { LogInViewModel(get(), get()) }
    single { AddExpenseViewModel(get(),get()) }
//    single { QRHelperImp() }
}