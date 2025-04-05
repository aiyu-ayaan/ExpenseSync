package com.atech.expensesync.ui.screens.scan

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.expensesync.database.models.DesktopLogInDetails
import com.atech.expensesync.database.pref.PrefKeys
import com.atech.expensesync.database.pref.PrefManager
import com.atech.expensesync.firebase.usecase.FirebaseUserUseCases
import com.atech.expensesync.firebase.util.FirebaseResponse
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ScanViewModel(
    private val useCases: FirebaseUserUseCases, prefManager: PrefManager
) : ViewModel() {
    private val uid = prefManager.getString(PrefKeys.USER_ID)
    private val _logInDetails = mutableStateOf<FirebaseResponse<DesktopLogInDetails?>>(
        FirebaseResponse.Loading
    )
    val logInDetails: State<FirebaseResponse<DesktopLogInDetails?>> get() = _logInDetails

    init {
        getLogInDetails()
    }

    private fun getLogInDetails() = viewModelScope.launch {
        useCases.getLogInDetails(uid).onEach {
            when (it) {
                is FirebaseResponse.Error -> {
                    _logInDetails.value = it
                }

                is FirebaseResponse.Loading -> {
                    _logInDetails.value = it
                }

                is FirebaseResponse.Success -> {
                    _logInDetails.value = FirebaseResponse.Success(it.data.desktopLogInDetails)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: ScanEvents) {
        when (event) {
            is ScanEvents.OnScanSuccess -> viewModelScope.launch {

                val key = event.key.split("$")[0]
                val os = event.key.split("$")[1]
                val model = DesktopLogInDetails(
                    systemUid = key, systemName = os
                )
                event.onDone(
                    useCases.logInToDesktopUseCase.invoke(
                        uid = uid, model = model
                    )
                )
            }

            ScanEvents.PerformLogOut -> viewModelScope.launch {
                useCases.performDesktopLogOut.invoke(uid)
            }
        }
    }
}