package com.atech.expensesync.modules

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.atech.expensesync.database.pref.PrefManager
import com.atech.expensesync.database.pref.PrefManager.Companion.PREF_NAME
import com.atech.expensesync.database.pref.PrefManagerImp
import com.atech.expensesync.database.pref.createDataStore
import com.atech.expensesync.database.room.ExpenseSyncDatabase
import com.atech.expensesync.database.room.getExpenseSyncDatabase
import com.atech.expensesync.utils.getAppDataPath
import org.koin.dsl.bind
import org.koin.dsl.module
import java.nio.file.Paths

val jvmModule = module {
    single { getExpenseSyncDatabase() }.bind(ExpenseSyncDatabase::class)
    single<DataStore<Preferences>> {
        createDataStore {
            val appDataPath = getAppDataPath()
            Paths.get(appDataPath, PREF_NAME).toString()
        }
    }
    single { PrefManagerImp(get()) }.bind(PrefManager::class)
}