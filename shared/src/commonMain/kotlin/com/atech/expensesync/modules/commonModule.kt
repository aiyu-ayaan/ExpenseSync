package com.atech.expensesync.modules

import com.atech.expensesync.database.room.SplitSyncDatabase
import com.atech.expensesync.firebase.usecase.FirebaseUserUseCases
import com.atech.expensesync.firebase.usecase.GetMealBookDataUseCases
import com.atech.expensesync.firebase.usecase.MealBookUploadUseCase
import com.atech.expensesync.uploadData.GetAllUnUploadByTypeUseCases
import com.atech.expensesync.uploadData.GetAllUnUploadUseCases
import com.atech.expensesync.uploadData.GetAllUseCases
import com.atech.expensesync.uploadData.InsertUploadUseCases
import com.atech.expensesync.uploadData.MealBookDataSyncUseCases
import com.atech.expensesync.uploadData.MealBookEntryDataSyncUseCases
import com.atech.expensesync.uploadData.MealBookSyncUseCases
import com.atech.expensesync.uploadData.UpdateUploadUseCases
import com.atech.expensesync.uploadData.UploadDataHelper
import com.atech.expensesync.uploadData.UploadUseCases
import com.atech.expensesync.usecases.AddTransactionUseCase
import com.atech.expensesync.usecases.CreateMealBook
import com.atech.expensesync.usecases.CreateMealBookEntry
import com.atech.expensesync.usecases.CreateNewGroupUseCase
import com.atech.expensesync.usecases.CreateNewTransactionUseCase
import com.atech.expensesync.usecases.DeleteExpenseEntryUseCase
import com.atech.expensesync.usecases.DeleteExpenseUseCase
import com.atech.expensesync.usecases.DeleteGroupUseCase
import com.atech.expensesync.usecases.DeleteMealBook
import com.atech.expensesync.usecases.DeleteMealBookEntry
import com.atech.expensesync.usecases.DeleteTransactionUseCase
import com.atech.expensesync.usecases.ExpenseUseCases
import com.atech.expensesync.usecases.GetAllExpensesUseCase
import com.atech.expensesync.usecases.GetExpenseBookEntryUseCase
import com.atech.expensesync.usecases.GetExpenseByIdUseCase
import com.atech.expensesync.usecases.GetGroupMembers
import com.atech.expensesync.usecases.GetGroupsUseCase
import com.atech.expensesync.usecases.GetMealBookEntries
import com.atech.expensesync.usecases.GetMealBooks
import com.atech.expensesync.usecases.GetTotalPrice
import com.atech.expensesync.usecases.GetTransactionsUseCase
import com.atech.expensesync.usecases.InsertExpenseEntryUseCase
import com.atech.expensesync.usecases.InsertExpenseUseCase
import com.atech.expensesync.usecases.InsertMember
import com.atech.expensesync.usecases.MapTransactionWithSplitAndThenUser
import com.atech.expensesync.usecases.MealBookUseCases
import com.atech.expensesync.usecases.RemoveMember
import com.atech.expensesync.usecases.RestoreData
import com.atech.expensesync.usecases.RestoreMealData
import com.atech.expensesync.usecases.SplitGroupMemberUseCases
import com.atech.expensesync.usecases.SplitTransactionUseCases
import com.atech.expensesync.usecases.SplitUseCases
import com.atech.expensesync.usecases.UpdateExpenseEntryUseCase
import com.atech.expensesync.usecases.UpdateExpenseUseCase
import com.atech.expensesync.usecases.UpdateGroupUseCase
import com.atech.expensesync.usecases.UpdateMealBook
import com.atech.expensesync.usecases.UpdateMealBookEntry
import com.atech.expensesync.usecases.UpdateTotalAmountUseCase
import com.atech.expensesync.usecases.UpdateTotalInUseCase
import com.atech.expensesync.usecases.UpdateTotalOutUseCase
import com.atech.expensesync.usecases.UpdateTransactionUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val commonModule = module {
    // Dao
    single { get<SplitSyncDatabase>().splitGroupDao }
    single { get<SplitSyncDatabase>().transactionSplitDao }
    single { get<SplitSyncDatabase>().splitGroupMemberDao }
    single { get<SplitSyncDatabase>().splitTransactionDao }
    single { get<SplitSyncDatabase>().mealDao }
    single { get<SplitSyncDatabase>().expenseBookDao }
    single { get<SplitSyncDatabase>().updateDao }
    single { get<SplitSyncDatabase>().maintenanceDao }
    single { CreateNewGroupUseCase(get()) }
    single { UpdateGroupUseCase(get()) }
    single { GetGroupsUseCase(get()) }
    single { DeleteGroupUseCase(get()) }

    single { SplitUseCases(get(), get(), get(), get()) }

    single { InsertMember(get()) }
    single { GetGroupMembers(get()) }
    single { RemoveMember(get()) }
    single { SplitGroupMemberUseCases(get(), get(), get()) }

    single { CreateNewTransactionUseCase(get()) }
    single { GetTransactionsUseCase(get()) }
    single { UpdateTransactionUseCase(get()) }
    single { DeleteTransactionUseCase(get()) }
    single { MapTransactionWithSplitAndThenUser(get()) }
    single { SplitTransactionUseCases(get(), get(), get(), get(), get()) }
    single { CreateMealBook(get(), get()) }
    single { CreateMealBookEntry(get(), get()) }
    single { GetMealBooks(get()) }
    single { GetMealBookEntries(get()) }
    single { UpdateMealBook(get(), get()) }
    single { UpdateMealBookEntry(get(), get()) }
    single { DeleteMealBook(get(), get()) }
    single { DeleteMealBookEntry(get(), get()) }
    single { GetTotalPrice(get()) }
    single { MealBookUseCases(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    single { InsertExpenseUseCase(get()) }
    single { InsertExpenseEntryUseCase(get()) }
    single { UpdateExpenseUseCase(get()) }
    single { UpdateExpenseEntryUseCase(get()) }
    single { DeleteExpenseUseCase(get()) }
    single { DeleteExpenseEntryUseCase(get()) }
    single { GetAllExpensesUseCase(get()) }
    single { GetExpenseByIdUseCase(get()) }
    single { GetExpenseBookEntryUseCase(get()) }
    single { UpdateTotalAmountUseCase(get()) }
    single { UpdateTotalInUseCase(get()) }
    single { UpdateTotalOutUseCase(get()) }
    single { AddTransactionUseCase(get()) }
    single {
        ExpenseUseCases(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }
    single { com.atech.expensesync.firebase.usecase.CreateUserUseCase(get()) }
    single { com.atech.expensesync.firebase.usecase.LogInToDesktopUseCase(get()) }
    single { com.atech.expensesync.firebase.usecase.ObserveLogInUsingOR(get()) }
    single { com.atech.expensesync.firebase.usecase.GetLogInDetails(get()) }
    single { com.atech.expensesync.firebase.usecase.PerformDesktopLogOut(get()) }
    single { com.atech.expensesync.firebase.usecase.GetUserDetails(get()) }
    single { FirebaseUserUseCases(get(), get(), get(), get(), get(), get()) }
    single { InsertUploadUseCases(get()) }
    single { UpdateUploadUseCases(get()) }
    single { GetAllUnUploadByTypeUseCases(get()) }
    single { GetAllUnUploadUseCases(get()) }
    single { GetAllUseCases(get()) }
    single { UploadUseCases(get(), get(), get(), get(), get()) }
    single { MealBookUploadUseCase(get(), get()) }
    single { GetMealBookDataUseCases(get()) }
    single { CoroutineScope(SupervisorJob() + Dispatchers.IO) }

    single { MealBookDataSyncUseCases(get(), get()) }
    single { MealBookEntryDataSyncUseCases(get(), get()) }
    single { MealBookSyncUseCases(get(), get()) }

    single { RestoreMealData(get(), get()) }
    single { RestoreData(get()) }
    single { UploadDataHelper(get(), get(), get(), get()) }

}
