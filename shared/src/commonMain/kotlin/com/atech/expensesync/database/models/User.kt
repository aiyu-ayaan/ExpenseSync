package com.atech.expensesync.database.models

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class DesktopLogInDetails(
    val systemUid: String = "",
    val systemName: String = "",
    val longInAt: Long = System.currentTimeMillis(),
)

@Keep
@Serializable
data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val desktopLogInDetails: DesktopLogInDetails? = null,
    val systemUid: String? = null,
)