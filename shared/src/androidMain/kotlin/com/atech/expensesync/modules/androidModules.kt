package com.atech.expensesync.modules

import com.atech.expensesync.database.getExpenseSyncDatabase
import org.koin.dsl.module

val androidModules = module {
    single {
        getExpenseSyncDatabase(get())
    }
}