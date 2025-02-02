package com.atech.expensesync.database.models

import kotlinx.serialization.Serializable


@Serializable
data class DesktopLogInDetails(
    val systemUid: String = "",
    val systemName: String = "",
    val longInAt: Long = System.currentTimeMillis(),
)

@Serializable
data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val desktopLogInDetails: DesktopLogInDetails? = null,
)