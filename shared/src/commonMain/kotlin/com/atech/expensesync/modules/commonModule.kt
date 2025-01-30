package com.atech.expensesync.modules

import com.atech.expensesync.database.room.ExpenseSyncDatabase
import com.atech.expensesync.database.room.split.SplitDao
import com.atech.expensesync.usecases.CreateNewGroupUseCase
import com.atech.expensesync.usecases.DeleteGroupUseCase
import com.atech.expensesync.usecases.GetGroupsUseCase
import com.atech.expensesync.usecases.SplitUseCases
import com.atech.expensesync.usecases.UpdateGroupUseCase
import org.koin.dsl.module

val commonModule = module {
    // Dao
    single<SplitDao> { get<ExpenseSyncDatabase>().splitGroupDao() }
    single { CreateNewGroupUseCase(get()) }
    single { UpdateGroupUseCase(get()) }
    single { GetGroupsUseCase(get()) }
    single { DeleteGroupUseCase(get()) }

    single { SplitUseCases(get(), get(), get(), get()) }
}