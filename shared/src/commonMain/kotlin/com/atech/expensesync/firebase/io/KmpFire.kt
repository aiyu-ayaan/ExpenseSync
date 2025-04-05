package com.atech.expensesync.firebase.io

import com.atech.expensesync.firebase.util.FirebaseResponse
import kotlinx.coroutines.flow.Flow

expect class KmpFire {

    suspend inline fun <reified T : Any> getObservedData(
        collectionName: String,
        documentName: String,
    ): Flow<FirebaseResponse<T>>

    suspend inline fun <reified T : Any> getObservedCollection(
        collectionName: String,
    ): Flow<FirebaseResponse<List<T>>>

    suspend inline fun <reified T : Any> getData(
        collectionName: String,
        documentName: String,
    ): FirebaseResponse<T>


    suspend inline fun <reified T : Any> insertData(
        collectionName: String,
        documentName: String?,
        data: T
    ): FirebaseResponse<T>

    suspend inline fun <reified T : Any> updateDataModel(
        collectionName: String,
        documentName: String,
        data: T
    ): FirebaseResponse<T>

    suspend inline fun <reified T : Any> updateDataMap(
        collectionName: String,
        documentName: String,
        data: Map<String, Any>
    ): FirebaseResponse<T>
}