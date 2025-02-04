package com.atech.expensesync.koin

import com.atech.expensesync.ui.screens.login.LogInViewModel
import com.atech.expensesync.ui.screens.scan.ScanViewModel
import com.atech.expensesync.ui.screens.split.SplitViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidUIModule = module {
    viewModel { SplitViewModel(get()) }
    viewModel { LogInViewModel(get(),get()) }
    viewModel { ScanViewModel(get(), get()) }

}