package com.atech.expensesync.firebase

import com.atech.expensesync.database.models.User
import com.atech.expensesync.utils.FirebaseCollectionPath
import com.google.cloud.firestore.Firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class CreateUser(
    private val db: Firestore = FirebaseInstance.firestore,
) {
    suspend operator fun invoke(
        model: User
    ) = withContext(Dispatchers.IO) {
        try {
            db.collection(
                FirebaseCollectionPath.USER.path
            ).document(model.uid).set(model).get()
            model.uid
        } catch (e: Exception) {
            throw Exception("Failed to create user: ${e.message}")
        }
    }
}