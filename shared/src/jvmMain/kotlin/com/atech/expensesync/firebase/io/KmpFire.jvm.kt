package com.atech.expensesync.firebase.io

import com.atech.expensesync.firebase.util.FirebaseResponse
import com.google.cloud.firestore.Firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

actual class KmpFire(
    val firestore: Firestore
) {
    actual suspend inline fun <reified T : Any> fetchData(
        collectionName: String,
        documentName: String,
    ): Flow<FirebaseResponse<T>> = callbackFlow {
        trySend(FirebaseResponse.Loading)
        try {

            val docRef = firestore.collection(collectionName).document(documentName)

            val listener = docRef.addSnapshotListener { snapshot, error ->
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


            awaitClose {
                listener.remove()
            }
        } catch (e: Exception) {
            trySend(FirebaseResponse.Error("Failed to set up listener: ${e.message}"))
            close()
        }
    }

    actual suspend inline fun <reified T : Any> fetchCollectionData(
        collectionName: String,
    ): Flow<FirebaseResponse<List<T>>> = callbackFlow {
        trySend(FirebaseResponse.Loading)
        try {
            val collectionRef = firestore.collection(collectionName)

            val listener = collectionRef.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(FirebaseResponse.Error("Failed to listen for changes: ${error.message}"))
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val data = snapshot.toObjects(T::class.java)
                    trySend(FirebaseResponse.Success(data))
                } else {
                    trySend(FirebaseResponse.Error("No data found"))
                }
            }

            awaitClose {
                listener.remove()
            }
        } catch (e: Exception) {
            trySend(FirebaseResponse.Error("Failed to set up listener: ${e.message}"))
            close()
        }
    }
}