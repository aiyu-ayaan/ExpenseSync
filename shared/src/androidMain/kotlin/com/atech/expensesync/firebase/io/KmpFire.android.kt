package com.atech.expensesync.firebase.io

import com.atech.expensesync.firebase.helper.FirebaseHelper
import com.atech.expensesync.firebase.helper.buildCollectionRef
import com.atech.expensesync.firebase.util.FirebaseResponse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

actual class KmpFire(
    val firestore: FirebaseFirestore
) {
    actual suspend inline fun <reified T : Any> getObservedData(
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


    actual suspend inline fun <reified T : Any> getObservedDataWithQuery(
        vararg firebaseHelper: FirebaseHelper,
    ): Flow<FirebaseResponse<T>> = callbackFlow {
        trySend(FirebaseResponse.Loading)
        try {
            // Build the collection reference using the provided FirebaseHelper instances
            val collectionRef = buildCollectionRef(*firebaseHelper, firestore = firestore)

            // Create listener for document changes

            val listenerRegistration = collectionRef.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(FirebaseResponse.Error("Failed to listen for changes: ${error.message}"))
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.documents.isNotEmpty()) {
                    val document = snapshot.documents.first()
                    if (document != null) {
                        val data = document.toObject(T::class.java)
                        if (data != null) {
                            trySend(FirebaseResponse.Success(data))
                        } else {
                            trySend(FirebaseResponse.Error("Failed to convert data"))
                        }
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

    actual suspend inline fun <reified T : Any> getObservedCollection(
        collectionName: String,
    ): Flow<FirebaseResponse<List<T>>> = callbackFlow {
        trySend(FirebaseResponse.Loading)

        try {
            // Get reference to the collection
            val collectionRef = firestore.collection(collectionName)

            // Create listener for collection changes
            val listenerRegistration = collectionRef.addSnapshotListener { snapshot, error ->
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

            // Cancel the listener when the Flow collection is stopped
            awaitClose {
                listenerRegistration.remove()
            }
        } catch (e: Exception) {
            trySend(FirebaseResponse.Error("Failed to set up listener: ${e.message}"))
            close()
        }
    }

    actual suspend inline fun <reified T : Any> getObservedDataWithArrayContains(
        collectionName: String,
        query: Pair<String, String>
    ): Flow<FirebaseResponse<List<T>>> = callbackFlow{
        trySend(FirebaseResponse.Loading)
        try {
            val collectionRef = firestore.collection(collectionName)
            val query = collectionRef.whereArrayContains(query.first, query.second)

            val listenerRegistration = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(FirebaseResponse.Error("Failed to listen for changes: ${error.message}"))
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.documents.isNotEmpty()) {
                    val data = snapshot.toObjects(T::class.java)
                    trySend(FirebaseResponse.Success(data))
                } else {
                    trySend(FirebaseResponse.Empty)
                }
            }

            awaitClose {
                listenerRegistration.remove()
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
        val snapshot = docRef.get().await()
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
        collectionName: String, documentName: String?, data: T
    ): FirebaseResponse<T> = try {
        val docRef =
            if (documentName != null) firestore.collection(collectionName).document(documentName)
            else firestore.collection(collectionName).document()
        docRef.set(data).await()
        FirebaseResponse.Success(data)
    } catch (e: Exception) {
        FirebaseResponse.Error("Failed to insert data: ${e.message}")
    }


    actual suspend inline fun <reified T : Any> updateDataModel(
        collectionName: String, documentName: String, data: T
    ): FirebaseResponse<T> = try {
        val docRef = firestore.collection(collectionName).document(documentName)
        docRef.set(data).await()
        FirebaseResponse.Success(data)
    } catch (e: Exception) {
        FirebaseResponse.Error("Failed to update data: ${e.message}")
    }

    actual suspend inline fun <reified T : Any> updateDataMap(
        collectionName: String, documentName: String, data: Map<String, Any?>
    ): FirebaseResponse<T> = try {
        val docRef = firestore.collection(collectionName).document(documentName)
        docRef.update(data).await()
        FirebaseResponse.Success(data as T)
    } catch (e: Exception) {
        FirebaseResponse.Error("Failed to update data: ${e.message}")
    }

}
