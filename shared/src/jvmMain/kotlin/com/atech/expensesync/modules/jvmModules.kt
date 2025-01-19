package com.atech.expensesync.modules

import com.atech.expensesync.database.getExpenseSyncDatabase
import org.koin.dsl.module

val jvmModule = module {
    single { getExpenseSyncDatabase() }
}