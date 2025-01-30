package com.atech.expensesync.koin

import com.atech.expensesync.ui.compose.split.SplitViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidUIModule = module {
    viewModel { SplitViewModel(get()) }
}