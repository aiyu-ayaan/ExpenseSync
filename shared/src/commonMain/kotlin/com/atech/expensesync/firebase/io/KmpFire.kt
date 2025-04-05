package com.atech.expensesync.firebase.io

import com.atech.expensesync.firebase.util.FirebaseResponse
import kotlinx.coroutines.flow.Flow

expect class KmpFire {

    suspend inline fun <reified T : Any> fetchData(
        collectionName: String,
        documentName: String,
    ): Flow<FirebaseResponse<T>>
}