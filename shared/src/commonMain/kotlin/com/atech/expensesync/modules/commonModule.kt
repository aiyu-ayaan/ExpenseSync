package com.atech.expensesync.modules

import com.atech.expensesync.database.ktor.ExpenseSyncClient
import com.atech.expensesync.database.ktor.ExpenseSyncClientImp
import com.atech.expensesync.database.room.ExpenseSyncDatabase
import com.atech.expensesync.usecases.CreateNewGroupUseCase
import com.atech.expensesync.usecases.CreateUserUseCase
import com.atech.expensesync.usecases.DeleteGroupUseCase
import com.atech.expensesync.usecases.GetGroupsUseCase
import com.atech.expensesync.usecases.SplitUseCases
import com.atech.expensesync.usecases.UpdateGroupUseCase
import com.atech.expensesync.usecases.UserUseCases
import org.koin.dsl.bind
import org.koin.dsl.module

val commonModule = module {
    // Dao
    single { get<ExpenseSyncDatabase>().expanseGroupDao }
    single { get<ExpenseSyncDatabase>().transactionSplitDao }
    single { get<ExpenseSyncDatabase>().expanseGroupMemberDao }
    single { get<ExpenseSyncDatabase>().expanseTransactionDao }
    single { CreateNewGroupUseCase(get()) }
    single { UpdateGroupUseCase(get()) }
    single { GetGroupsUseCase(get()) }
    single { DeleteGroupUseCase(get()) }

    single { SplitUseCases(get(), get(), get(), get()) }
    single { ExpenseSyncClientImp(
        com.atech.expensesync.database.ktor.httpClientEngineFactory().createEngine()
    ) }.bind(ExpenseSyncClient::class)

    single { CreateUserUseCase(get()) }

    single { UserUseCases(get()) }
}
