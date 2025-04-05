package com.atech.expensesync.firebase.io

import com.atech.expensesync.firebase.util.FirebaseResponse
import com.google.cloud.firestore.Firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

actual class KmpFire(
    val firestore: Firestore
) {
    actual suspend inline fun <reified T : Any> getObservedData(
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

    actual suspend inline fun <reified T : Any> getObservedCollection(
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

    actual suspend inline fun <reified T : Any> getData(
        collectionName: String,
        documentName: String,
    ): FirebaseResponse<T> = try {
        val docRef = firestore.collection(collectionName).document(documentName)
        val snapshot = docRef.get().get()
        if (snapshot.exists()) {
            val data = snapshot.toObject(T::class.java)
            if (data != null) {
                FirebaseResponse.Success(data)
            } else {
                FirebaseResponse.Error("Failed to convert data")
            }
        } else {
            FirebaseResponse.Error("No data found")
        }
    } catch (e: Exception) {
        FirebaseResponse.Error("Failed to get data: ${e.message}")
    }

    actual suspend inline fun <reified T : Any> insertData(
        collectionName: String,
        documentName: String?,
        data: T
    ): FirebaseResponse<T> = try {
        val docRef = if (documentName != null)
            firestore.collection(collectionName).document(documentName)
        else
            firestore.collection(collectionName).document()
        docRef.set(data).get()
        FirebaseResponse.Success(data)
    } catch (e: Exception) {
        FirebaseResponse.Error("Failed to insert data: ${e.message}")
    }
}