package com.atech.expensesync.koin

import com.atech.expensesync.ui.screens.expense.root.ExpenseViewModel
import com.atech.expensesync.ui.screens.login.LogInViewModel
import com.atech.expensesync.ui.screens.meal.root.MealViewModel
import com.atech.expensesync.ui.screens.meal.view.ViewMealViewModel
import com.atech.expensesync.ui.screens.profile.ProfileViewModel
import com.atech.expensesync.ui.screens.scan.ScanViewModel
import com.atech.expensesync.ui.screens.split.add.AddExpenseViewModel
import com.atech.expensesync.ui.screens.split.root.SplitViewModel
import org.koin.androidx.viewmodel.dsl.viewModel

import org.koin.dsl.module

val androidUIModule = module {
    viewModel { SplitViewModel(get(), get()) }
    viewModel { LogInViewModel(get()) }
    viewModel { ScanViewModel(get(), get()) }
    viewModel { AddExpenseViewModel(get(), get(), get()) }
    viewModel { MealViewModel(get(), get(), get()) }
    viewModel { ViewMealViewModel(get()) }
    viewModel { ExpenseViewModel(get()) }
    viewModel { com.atech.expensesync.ui.screens.backup.BackUpViewModel(get(), get()) }
    viewModel { ProfileViewModel(get(), get()) }
}