package com.atech.expensesync.modules

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.atech.expensesync.database.ktor.DesktopClientEngineFactory
import com.atech.expensesync.database.ktor.EngineFactory
import com.atech.expensesync.database.pref.PrefManager
import com.atech.expensesync.database.pref.PrefManager.Companion.PREF_NAME
import com.atech.expensesync.database.pref.PrefManagerImp
import com.atech.expensesync.database.pref.createDataStore
import com.atech.expensesync.database.room.SplitSyncDatabase
import com.atech.expensesync.database.room.getExpenseSyncDatabase
import com.atech.expensesync.firebase.FirebaseInstance
import com.atech.expensesync.firebase.io.KmpFire
import com.atech.expensesync.utils.getAppDataPath
import org.koin.dsl.bind
import org.koin.dsl.module
import java.nio.file.Paths

val jvmModule = module {
    single { getExpenseSyncDatabase() }.bind(SplitSyncDatabase::class)
    single<DataStore<Preferences>> {
        createDataStore {
            val appDataPath = getAppDataPath()
            Paths.get(appDataPath, PREF_NAME).toString()
        }
    }
    single {
        DesktopClientEngineFactory()
    }.bind(EngineFactory::class)
    single { PrefManagerImp(get()) }.bind(PrefManager::class)

    single {
        FirebaseInstance.firestore
    }

    single { KmpFire(get()) }
}