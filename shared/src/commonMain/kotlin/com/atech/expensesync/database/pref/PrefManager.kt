package com.atech.expensesync.database.pref

/**
 * PrefManager
 * This interface is used to manage the preferences
 */
interface PrefManager {
    /**
     * Save string
     * This function is used to save the string
     * @param key The key
     * @param value The value
     */
    fun saveString(key: PrefKeys, value: String)

    /**
     * Get string
     * This function is used to get the string
     * @param key The key
     * @return [String]
     */
    fun getString(key: PrefKeys): String

    /**
     * Save int
     * This function is used to save the int
     * @param key The key
     * @param value The value
     */
    fun saveInt(key: PrefKeys, value: Int)

    /**
     * Get int
     * This function is used to get the int
     * @param key The key
     * @return [Int]
     */
    fun getInt(key: PrefKeys): Int

    /**
     * Save long
     * This function is used to save the long
     * @param key The key
     * @param value The value
     */
    fun saveBoolean(key: PrefKeys, value: Boolean)

    /**
     * Get boolean
     * This function is used to get the boolean
     * @param key The key
     * @return [Boolean]
     */
    fun getBoolean(key: PrefKeys): Boolean

    /**
     * Clear all
     * This function is used to clear all the preferences
     */
    fun clearAll()

    /**
     * Remove
     * This function is used to remove the preference
     * @param key The key
     */
    fun remove(key: PrefKeys)

    /**
     * Contains
     * This function is used to check if the preference contains the key
     * @param key The key
     * @return [Boolean]
     */
    fun contains(key: PrefKeys): Boolean

    companion object {
        const val PREF_NAME = "user.preferences_pb"
    }
}

/**
 * Prefs
 * This enum class is used to represent the preferences
 */
enum class PrefKeys {
    IS_LOG_IN_SKIP,
    USER_ID,
    USER_MODEL,
    DESKTOP_USER_UID,
}