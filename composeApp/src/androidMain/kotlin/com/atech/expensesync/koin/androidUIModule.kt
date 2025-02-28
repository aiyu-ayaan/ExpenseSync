package com.atech.expensesync.koin

import com.atech.expensesync.ui.screens.login.LogInViewModel
import com.atech.expensesync.ui.screens.meal.root.MealViewModel
import com.atech.expensesync.ui.screens.scan.ScanViewModel
import com.atech.expensesync.ui.screens.split.add.AddExpenseViewModel
import com.atech.expensesync.ui.screens.split.root.SplitViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidUIModule = module {
    viewModel { SplitViewModel(get(), get()) }
    viewModel { LogInViewModel(get(), get()) }
    viewModel { ScanViewModel(get(), get()) }
    viewModel { AddExpenseViewModel(get(), get(), get()) }
    viewModel { MealViewModel() }
}