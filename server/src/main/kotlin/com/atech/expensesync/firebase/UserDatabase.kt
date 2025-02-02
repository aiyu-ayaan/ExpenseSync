package com.atech.expensesync.firebase

import com.atech.expensesync.database.models.User
import com.atech.expensesync.utils.FirebaseCollectionPath
import com.google.cloud.firestore.Firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class CheckUser(
    private val db: Firestore = FirebaseInstance.firestore,
) {
    suspend operator fun invoke(
        uid: String
    ) = withContext(Dispatchers.IO) {
        try {
            db.collection(
                FirebaseCollectionPath.USER.path
            ).document(uid).get().get().toObject(User::class.java)
        } catch (e: Exception) {
            throw Exception("Failed to check user: ${e.message}")
        }
    }
}

data class CreateUser(
    private val db: Firestore = FirebaseInstance.firestore,
    private val checkUser: CheckUser = CheckUser()
) {
    suspend operator fun invoke(
        model: User
    ) = withContext(Dispatchers.IO) {
        try {
            if (checkUser(model.uid) != null) {
                return@withContext model
            }
            db.collection(
                FirebaseCollectionPath.USER.path
            ).document(model.uid).set(model).get()
            model
        } catch (e: Exception) {
            throw Exception("Failed to create user: ${e.message}")
        }
    }
}