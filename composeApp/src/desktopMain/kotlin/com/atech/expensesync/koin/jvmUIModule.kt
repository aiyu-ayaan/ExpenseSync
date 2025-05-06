package com.atech.expensesync.koin

import com.atech.expensesync.ui.screens.backup.BackUpViewModel
import com.atech.expensesync.ui.screens.expense.root.ExpenseViewModel
import com.atech.expensesync.ui.screens.login.LogInViewModel
import com.atech.expensesync.ui.screens.meal.root.MealViewModel
import com.atech.expensesync.ui.screens.meal.view.ViewMealViewModel
import com.atech.expensesync.ui.screens.profile.ProfileViewModel
import com.atech.expensesync.ui.screens.splitv2.details.SplitDetailsViewModel
import com.atech.expensesync.ui.screens.splitv2.root.SplitViewModel
import com.atech.expensesync.ui_utils.LinkHelper
import org.koin.dsl.module

val jvmUIModule = module {
    single { SplitViewModel(get(), get()) }
    single { LogInViewModel(get()) }
    single { MealViewModel(get(), get(), get()) }
    single { ViewMealViewModel(get()) }
    single { ExpenseViewModel(get(), get(), get()) }
    single { BackUpViewModel(get(), get()) }
    single { ProfileViewModel(get(), get()) }
    single { SplitViewModel(get(), get()) }
    single { SplitDetailsViewModel(get()) }
    single { LinkHelper() }
//    single { QRHelperImp() }
}