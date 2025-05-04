package com.atech.expensesync.ui.screens.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.expensesync.database.models.User
import com.atech.expensesync.firebase.usecase.FirebaseUserUseCases
import com.atech.expensesync.firebase.util.getOrNull
import com.atech.expensesync.firebase.util.isSuccess
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LogInViewModel(
    private val firebaseUserUseCase: FirebaseUserUseCases
) : ViewModel() {

    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> get() = _user

    fun onEvent(events: LogInEvents) {
        when (events) {
            is LogInEvents.OnLogInClicked -> viewModelScope.launch {
                events.onSuccess(firebaseUserUseCase.createUser(events.model))
            }

            is LogInEvents.ObserveLogInData -> viewModelScope.launch {
                firebaseUserUseCase
                    .observeLogInUsingOR(events.desktopUid)
                    .onEach {
                        if (it.isSuccess()) {
                            _user.value = it.getOrNull()
                        }
                    }.launchIn(viewModelScope)
            }

            LogInEvents.ResetData -> viewModelScope.launch {
                _user.value = null
            }
        }
    }
}