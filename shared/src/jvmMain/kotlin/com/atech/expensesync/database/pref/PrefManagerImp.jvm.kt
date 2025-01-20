package com.atech.expensesync.database.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class PrefManagerImp(
    private val pref: DataStore<Preferences>
) : PrefManager {
    override fun saveString(
        key: PrefKeys,
        value: String
    ): Unit =
        runBlocking {
            pref.edit {
                it[stringPreferencesKey(key.name)] = value
            }
        }


    override fun getString(key: PrefKeys): String = runBlocking {
        pref.data.map {
            it[stringPreferencesKey(key.name)] ?: ""
        }.map { it }.first()
    }

    override fun saveInt(key: PrefKeys, value: Int): Unit =
        runBlocking {
            pref.edit {
                it[intPreferencesKey(key.name)] = value
            }
        }

    override fun getInt(key: PrefKeys): Int = runBlocking {
        pref.data.map {
            it[intPreferencesKey(key.name)] ?: 0
        }.map { it }.first()
    }

    override fun saveBoolean(
        key: PrefKeys,
        value: Boolean
    ): Unit = runBlocking {
        pref.edit {
            it[booleanPreferencesKey(key.name)] = value
        }
    }

    override fun getBoolean(key: PrefKeys): Boolean = runBlocking {
        pref.data.map {
            it[booleanPreferencesKey(key.name)] == true
        }.map { it }.first()
    }

    override fun clearAll() {
        runBlocking {
            pref.edit {
                it.clear()
            }
        }
    }

    override fun remove(key: PrefKeys) {
        runBlocking {
            pref.edit {
                it.remove(stringPreferencesKey(key.name))
            }
        }
    }

    override fun contains(key: PrefKeys): Boolean =
        runBlocking {
            pref.data.map {
                it.contains(stringPreferencesKey(key.name))
            }.map { it }.first()
        }

}