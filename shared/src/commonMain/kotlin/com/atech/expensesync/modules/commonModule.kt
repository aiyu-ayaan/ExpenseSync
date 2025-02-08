package com.atech.expensesync.modules

import com.atech.expensesync.database.ktor.http.ExpenseSyncClient
import com.atech.expensesync.database.ktor.http.ExpenseSyncClientImp
import com.atech.expensesync.database.ktor.httpClientEngineFactory
import com.atech.expensesync.database.ktor.websocket.UserDataWebSocket
import com.atech.expensesync.database.room.ExpenseSyncDatabase
import com.atech.expensesync.usecases.CreateNewGroupUseCase
import com.atech.expensesync.usecases.CreateUserUseCase
import com.atech.expensesync.usecases.DeleteGroupUseCase
import com.atech.expensesync.usecases.ExpanseGroupMemberUseCases
import com.atech.expensesync.usecases.GetGroupMembers
import com.atech.expensesync.usecases.GetGroupsUseCase
import com.atech.expensesync.usecases.InsertMember
import com.atech.expensesync.usecases.LogInToDesktopUseCase
import com.atech.expensesync.usecases.RemoveMember
import com.atech.expensesync.usecases.SplitUseCases
import com.atech.expensesync.usecases.UpdateGroupUseCase
import com.atech.expensesync.usecases.UserUseCases
import io.ktor.client.HttpClient
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
    single<HttpClient> { httpClientEngineFactory().createEngine() }
    single {
        ExpenseSyncClientImp(
            get()
        )
    }.bind(ExpenseSyncClient::class)

    single { CreateUserUseCase(get()) }
    single { LogInToDesktopUseCase(get()) }
    single { UserUseCases(get(), get()) }
    single { UserDataWebSocket(get()) }

    single { InsertMember(get()) }
    single { GetGroupMembers(get()) }
    single { RemoveMember(get()) }
    single { ExpanseGroupMemberUseCases(get(), get(), get()) }
}
