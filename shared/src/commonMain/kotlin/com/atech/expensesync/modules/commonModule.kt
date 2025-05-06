package com.atech.expensesync.modules

import com.atech.expensesync.database.room.SplitSyncDatabase
import com.atech.expensesync.firebase.usecase.CreateSplitGroup
import com.atech.expensesync.firebase.usecase.CreateTransaction
import com.atech.expensesync.firebase.usecase.FirebaseUserUseCases
import com.atech.expensesync.firebase.usecase.GetAllExpenseDataUseCases
import com.atech.expensesync.firebase.usecase.GetGlobalTransaction
import com.atech.expensesync.firebase.usecase.GetMealBookDataUseCases
import com.atech.expensesync.firebase.usecase.GetSplitById
import com.atech.expensesync.firebase.usecase.GetSplitData
import com.atech.expensesync.firebase.usecase.GetTransaction
import com.atech.expensesync.firebase.usecase.MealBookUploadUseCase
import com.atech.expensesync.firebase.usecase.SplitUseCases
import com.atech.expensesync.firebase.usecase.UploadExpenseDataUserCase
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
import com.atech.expensesync.usecases.DeleteExpenseEntryUseCase
import com.atech.expensesync.usecases.DeleteExpenseUseCase
import com.atech.expensesync.usecases.DeleteMealBook
import com.atech.expensesync.usecases.DeleteMealBookEntry
import com.atech.expensesync.usecases.ExpenseUseCases
import com.atech.expensesync.usecases.GetAllExpensesUseCase
import com.atech.expensesync.usecases.GetExpenseBookEntryUseCase
import com.atech.expensesync.usecases.GetExpenseByIdUseCase
import com.atech.expensesync.usecases.GetMealBookEntries
import com.atech.expensesync.usecases.GetMealBooks
import com.atech.expensesync.usecases.GetTotalPrice
import com.atech.expensesync.usecases.InsertExpenseEntryUseCase
import com.atech.expensesync.usecases.InsertExpenseUseCase
import com.atech.expensesync.usecases.MealBookUseCases
import com.atech.expensesync.usecases.RestoreData
import com.atech.expensesync.usecases.RestoreMealData
import com.atech.expensesync.usecases.UpdateExpenseEntryUseCase
import com.atech.expensesync.usecases.UpdateExpenseUseCase
import com.atech.expensesync.usecases.UpdateMealBook
import com.atech.expensesync.usecases.UpdateMealBookEntry
import com.atech.expensesync.usecases.UpdateTotalAmountUseCase
import com.atech.expensesync.usecases.UpdateTotalInUseCase
import com.atech.expensesync.usecases.UpdateTotalOutUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val commonModule = module {
    single { get<SplitSyncDatabase>().mealDao }
    single { get<SplitSyncDatabase>().expenseBookDao }
    single { get<SplitSyncDatabase>().updateDao }
    single { get<SplitSyncDatabase>().maintenanceDao }
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
    single { InsertExpenseUseCase(get(), get()) }
    single { InsertExpenseEntryUseCase(get(), get()) }
    single { UpdateExpenseUseCase(get(), get()) }
    single { UpdateExpenseEntryUseCase(get(), get()) }
    single { DeleteExpenseUseCase(get(), get()) }
    single { DeleteExpenseEntryUseCase(get(), get()) }
    single { GetAllExpensesUseCase(get()) }
    single { GetExpenseByIdUseCase(get()) }
    single { GetExpenseBookEntryUseCase(get()) }
    single { UpdateTotalAmountUseCase(get(), get()) }
    single { UpdateTotalInUseCase(get(), get()) }
    single { UpdateTotalOutUseCase(get(), get()) }
    single { AddTransactionUseCase(get(), get()) }
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
    single { UploadExpenseDataUserCase(get(), get()) }
    single { GetMealBookDataUseCases(get()) }
    single { GetAllExpenseDataUseCases(get()) }
    single { CoroutineScope(SupervisorJob() + Dispatchers.IO) }

    single { MealBookDataSyncUseCases(get(), get()) }
    single { MealBookEntryDataSyncUseCases(get(), get()) }
    single { MealBookSyncUseCases(get(), get()) }

    single { RestoreMealData(get(), get()) }
    single { RestoreData(get()) }
    single { UploadDataHelper(get(), get(), get(), get(), get()) }


    single { CreateSplitGroup(get()) }
    single { GetSplitData(get()) }
    single { GetSplitById(get()) }
    single { CreateTransaction(get()) }
    single { GetTransaction(get()) }
    single { GetGlobalTransaction(get()) }
    single { SplitUseCases(get(), get(), get(), get(), get(), get()) }

}
