package com.atech.expensesync.modules

import com.atech.expensesync.database.room.ExpenseSyncDatabase
import com.atech.expensesync.database.room.split.SplitDao
import org.koin.dsl.module

val commonModule = module {
    // Dao
    single<SplitDao> { get<ExpenseSyncDatabase>().splitGroupDao() }

}