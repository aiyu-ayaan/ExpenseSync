package com.atech.expensesync.ui.screens.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.expensesync.database.models.DesktopLogInDetails
import com.atech.expensesync.database.pref.PrefKeys
import com.atech.expensesync.database.pref.PrefManager
import com.atech.expensesync.usecases.LogInToDesktopUseCase
import kotlinx.coroutines.launch

class ScanViewModel(
    private val useCases: LogInToDesktopUseCase,
    private val prefManager: PrefManager
) : ViewModel() {
    fun onEvent(event: ScanEvents) {
        when (event) {
            is ScanEvents.OnScanSuccess -> viewModelScope.launch {
                val uid = prefManager.getString(PrefKeys.USER_ID)
                val key = event.key.split("$")[0]
                val os = event.key.split("$")[1]
                val model = DesktopLogInDetails(
                    systemUid = key,
                    systemName = os
                )
                event.onDone(
                    useCases.invoke(
                        uid = uid,
                        model = model
                    )
                )
            }
        }
    }
}