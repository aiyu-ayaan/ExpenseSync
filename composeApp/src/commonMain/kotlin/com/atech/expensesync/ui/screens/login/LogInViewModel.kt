package com.atech.expensesync.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.expensesync.usecases.UserUseCases
import kotlinx.coroutines.launch

class LogInViewModel(
    private val userUseCases: UserUseCases
) : ViewModel() {

    fun onEvent(events: LogInEvents) {
        when (events) {
            is LogInEvents.OnLogInClicked -> viewModelScope.launch {
                events.onSuccess(userUseCases.createUserUseCase(events.model))
            }
        }
    }
}