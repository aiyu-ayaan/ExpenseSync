package com.atech.expensesync.firebase.io

import com.atech.expensesync.firebase.util.FirebaseResponse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

actual class KmpFire(
    val firestore: FirebaseFirestore
) {
    actual suspend inline fun <reified T : Any> fetchData(
        collectionName: String,
        documentName: String,
    ): Flow<FirebaseResponse<T>> = callbackFlow {
        trySend(FirebaseResponse.Loading)

        try {
            // Get reference to the document
            val docRef = firestore.collection(collectionName).document(documentName)

            // Create listener for document changes
            val listenerRegistration = docRef.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(FirebaseResponse.Error("Failed to listen for changes: ${error.message}"))
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val data = snapshot.toObject(T::class.java)
                    if (data != null) {
                        trySend(FirebaseResponse.Success(data))
                    } else {
                        trySend(FirebaseResponse.Error("Failed to convert data"))
                    }
                } else {
                    trySend(FirebaseResponse.Error("No data found"))
                }
            }

            // Cancel the listener when the Flow collection is stopped
            awaitClose {
                listenerRegistration.remove()
            }
        } catch (e: Exception) {
            trySend(FirebaseResponse.Error("Failed to set up listener: ${e.message}"))
            close()
        }
    }
}