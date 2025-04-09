package com.atech.expensesync.utils

import com.atech.expensesync.firebase.util.FirebaseResponse
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

inline fun <ResultType, ResponseType> networkFetchData(
    crossinline query: suspend () -> Flow<ResultType>,
    crossinline fetch: suspend () -> Flow<FirebaseResponse<ResponseType>>,
    crossinline saveFetchResult: suspend (ResponseType) -> Unit,
    crossinline shouldFetch: () -> Boolean = { true },
): Flow<FirebaseResponse<ResultType>> = channelFlow {
    launch {
        query().collect { localData ->
            send(FirebaseResponse.Success(localData))
        }
    }

    // Handle network fetching if needed
    if (shouldFetch()) {
        launch {
            try {
                fetch().collect { response ->
                    when (response) {
                        is FirebaseResponse.Success -> {
                            saveFetchResult(response.data)
                            // Local data observation will emit updated data
                        }

                        is FirebaseResponse.Error -> {
                            send(FirebaseResponse.Error(response.error))
                        }

                        is FirebaseResponse.Loading -> {
                            // Already emitted loading state initially
                        }
                    }
                }
            } catch (e: Exception) {
                expenseSyncLogger("Exception during network fetch: ${e.message}")
                if (isActive) { // Check if channel is still active before sending
                    send(FirebaseResponse.Error(e.message ?: "Unknown error occurred"))
                }
            }
        }
    } else {
        expenseSyncLogger("Network fetch skipped based on shouldFetch condition")
    }
}


