package com.atech.expensesync.modules

import android.content.Context
import android.content.SharedPreferences
import com.atech.expensesync.database.ktor.AndroidClientEngineFactory
import com.atech.expensesync.database.ktor.EngineFactory
import com.atech.expensesync.database.pref.PrefManager
import com.atech.expensesync.database.pref.PrefManager.Companion.PREF_NAME
import com.atech.expensesync.database.pref.PrefManagerImp
import com.atech.expensesync.database.room.getExpenseSyncDatabase
import org.koin.dsl.bind
import org.koin.dsl.module

val androidModules = module {
    single {
        getExpenseSyncDatabase(get())
    }
    single<SharedPreferences> {
        get<Context>().getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )
    }
    single {
        AndroidClientEngineFactory(get())
    }.bind(EngineFactory::class)
    single { PrefManagerImp(get()) }.bind(PrefManager::class)
}