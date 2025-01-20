package com.atech.expensesync.database.pref

import android.content.SharedPreferences
import androidx.core.content.edit

class PrefManagerImp(
    private val prefManager: SharedPreferences
) : PrefManager {
    override fun saveString(
        key: PrefKeys,
        value: String
    ) = prefManager.edit { putString(key.name, value) }


    override fun getString(key: PrefKeys): String = prefManager.getString(key.name, "") ?: ""

    override fun saveInt(key: PrefKeys, value: Int) = prefManager.edit { putInt(key.name, value) }

    override fun getInt(key: PrefKeys): Int = prefManager.getInt(key.name, 0)

    override fun saveBoolean(
        key: PrefKeys,
        value: Boolean
    ) = prefManager.edit { putBoolean(key.name, value) }

    override fun getBoolean(key: PrefKeys): Boolean = prefManager.getBoolean(key.name, false)

    override fun clearAll() =
        prefManager.edit { clear() }


    override fun remove(key: PrefKeys) = prefManager.edit { remove(key.name) }

    override fun contains(key: PrefKeys): Boolean = prefManager.contains(key.name)

}