package com.atech.expensesync.koin

import com.atech.expensesync.ui.screens.expense.root.ExpenseViewModel
import com.atech.expensesync.ui.screens.login.LogInViewModel
import com.atech.expensesync.ui.screens.meal.root.MealViewModel
import com.atech.expensesync.ui.screens.meal.view.ViewMealViewModel
import com.atech.expensesync.ui.screens.split.add.AddExpenseViewModel
import com.atech.expensesync.ui.screens.split.root.SplitViewModel
import org.koin.dsl.module

val jvmUIModule = module {
    single { SplitViewModel(get(), get()) }
    single { LogInViewModel(get()) }
    single { AddExpenseViewModel(get(), get(), get()) }
    single { MealViewModel(get()) }
    single { ViewMealViewModel(get()) }
    single { ExpenseViewModel(get()) }
//    single { QRHelperImp() }
}