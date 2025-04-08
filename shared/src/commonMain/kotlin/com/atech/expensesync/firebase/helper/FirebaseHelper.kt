package com.atech.expensesync.firebase.helper

data class FirebaseHelper(
    val collectionName: String? = null,
    val documentName: String? = null,
    val queries: Pair<String, Any>? = null,
    val documentRef: String? = null,
)