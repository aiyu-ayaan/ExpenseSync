package com.atech.expensesync.ui.screens.backup

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.expensesync.database.pref.PrefKeys
import com.atech.expensesync.database.pref.PrefManager
import com.atech.expensesync.usecases.BackUpState
import com.atech.expensesync.usecases.RestoreData
import com.atech.expensesync.utils.expenseSyncLogger
import kotlinx.coroutines.launch

class BackUpViewModel(
    private val restoreData: RestoreData,
    pref: PrefManager
) : ViewModel() {

    private val _isBackUpDone = mutableStateOf(false)
    val isBackUpDone: State<Boolean> get() = _isBackUpDone
    private val uid = pref.getString(
        PrefKeys.USER_ID
    )

    init {
        onEvent(BackUpScreenEvents.OnMealDataBackUpDone)
    }

    fun onEvent(
        event: BackUpScreenEvents
    ) {
        when (event) {
            is BackUpScreenEvents.OnMealDataBackUpDone -> viewModelScope.launch {
                restoreData.restoreMealData.invoke(
                    uid = uid,
                    status = { status ->
                        checkStatus(status, onDone = {
                            expenseSyncLogger(
                                "Meal data restored successfully"
                            )
                            onEvent(
                                BackUpScreenEvents.OnExpenseDataBackUpDone
                            )
                        }, onError = {})
                    }
                )
            }

            is BackUpScreenEvents.OnExpenseDataBackUpDone -> {
                onEvent(
                    BackUpScreenEvents.OnSplitDataBackUpDone
                )
            }

            is BackUpScreenEvents.OnSplitDataBackUpDone -> {
                onEvent(
                    BackUpScreenEvents.OnDataBackUpDone
                )
            }

            is BackUpScreenEvents.OnDataBackUpDone -> {
                _isBackUpDone.value = true
            }
        }
    }

    private fun checkStatus(
        status: BackUpState,
        onError: (Exception) -> Unit = {},
        onDone: () -> Unit
    ) {
        when (status) {
            is BackUpState.OnError -> onError.invoke(status.exception)
            BackUpState.OnSuccess -> onDone.invoke()
        }
    }
}