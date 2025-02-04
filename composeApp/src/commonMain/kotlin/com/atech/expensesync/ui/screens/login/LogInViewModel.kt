package com.atech.expensesync.ui.screens.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.expensesync.database.ktor.websocket.UserDataWebSocket
import com.atech.expensesync.database.models.User
import com.atech.expensesync.usecases.UserUseCases
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LogInViewModel(
    private val userUseCases: UserUseCases,
    private val userDataWebSocket: UserDataWebSocket
) : ViewModel() {

    private val _user = mutableStateOf<User?>(User())
    val user: State<User?> get() = _user

    fun onEvent(events: LogInEvents) {
        when (events) {
            is LogInEvents.OnLogInClicked -> viewModelScope.launch {
                events.onSuccess(userUseCases.createUserUseCase(events.model))
            }

            is LogInEvents.StartWebSocket -> viewModelScope.launch {
                userDataWebSocket.getStateStream(events.desktopUid)
                    .onEach {
                        _user.value = it
                    }
                    .launchIn(viewModelScope)
            }


            is LogInEvents.StopWebSocket ->
                viewModelScope.launch {
                    events.action()
                }

        }
    }
}