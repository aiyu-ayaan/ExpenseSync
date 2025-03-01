package com.atech.expensesync.modules

import com.atech.expensesync.database.ktor.http.ExpenseSyncClient
import com.atech.expensesync.database.ktor.http.ExpenseSyncClientImp
import com.atech.expensesync.database.ktor.httpClientEngineFactory
import com.atech.expensesync.database.ktor.websocket.UserDataWebSocket
import com.atech.expensesync.database.room.ExpenseSyncDatabase
import com.atech.expensesync.usecases.CreateMealBook
import com.atech.expensesync.usecases.CreateMealBookEntry
import com.atech.expensesync.usecases.CreateNewGroupUseCase
import com.atech.expensesync.usecases.CreateNewTransactionUseCase
import com.atech.expensesync.usecases.CreateUserUseCase
import com.atech.expensesync.usecases.DeleteGroupUseCase
import com.atech.expensesync.usecases.DeleteMealBook
import com.atech.expensesync.usecases.DeleteMealBookEntry
import com.atech.expensesync.usecases.DeleteTransactionUseCase
import com.atech.expensesync.usecases.ExpanseGroupMemberUseCases
import com.atech.expensesync.usecases.ExpenseTransactionUseCases
import com.atech.expensesync.usecases.GetGroupMembers
import com.atech.expensesync.usecases.GetGroupsUseCase
import com.atech.expensesync.usecases.GetMealBookEntries
import com.atech.expensesync.usecases.GetMealBooks
import com.atech.expensesync.usecases.GetTransactionsUseCase
import com.atech.expensesync.usecases.InsertMember
import com.atech.expensesync.usecases.LogInToDesktopUseCase
import com.atech.expensesync.usecases.MapTransactionWithSplitAndThenUser
import com.atech.expensesync.usecases.MealBookUseCases
import com.atech.expensesync.usecases.RemoveMember
import com.atech.expensesync.usecases.SplitUseCases
import com.atech.expensesync.usecases.UpdateGroupUseCase
import com.atech.expensesync.usecases.UpdateMealBook
import com.atech.expensesync.usecases.UpdateMealBookEntry
import com.atech.expensesync.usecases.UpdateTransactionUseCase
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
    single { get<ExpenseSyncDatabase>().mealDao }
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

    single { CreateNewTransactionUseCase(get()) }
    single { GetTransactionsUseCase(get()) }
    single { UpdateTransactionUseCase(get()) }
    single { DeleteTransactionUseCase(get()) }
    single { MapTransactionWithSplitAndThenUser(get()) }
    single { ExpenseTransactionUseCases(get(), get(), get(), get(), get()) }
    single { CreateMealBook(get()) }
    single { CreateMealBookEntry(get()) }
    single { GetMealBooks(get()) }
    single { GetMealBookEntries(get()) }
    single { UpdateMealBook(get()) }
    single { UpdateMealBookEntry(get()) }
    single { DeleteMealBook(get()) }
    single { DeleteMealBookEntry(get()) }
    single { MealBookUseCases(get(), get(), get(), get(), get(), get(), get(), get()) }
}
