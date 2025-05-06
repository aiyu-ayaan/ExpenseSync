package com.atech.expensesync.ui.screens.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.expensesync.database.models.User
import com.atech.expensesync.database.pref.PrefKeys
import com.atech.expensesync.database.pref.PrefManager
import com.atech.expensesync.database.room.MaintenanceDao
import com.atech.expensesync.firebase.usecase.FirebaseUserUseCases
import com.atech.expensesync.firebase.util.FirebaseResponse
import com.atech.expensesync.uploadData.UploadUseCases
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class ProfileViewModel(
    private val useCase: FirebaseUserUseCases,
    private val pref: PrefManager,
    uploadUseCase: UploadUseCases,
    private val maintenanceDao: MaintenanceDao,
) : ViewModel() {
    private val _user = mutableStateOf<FirebaseResponse<User>>(FirebaseResponse.Loading)
    val user: State<FirebaseResponse<User>> get() = _user

    val uploadModel = uploadUseCase.getAll.invoke()


    init {
        getUserDetails()
    }

    fun performLogOut(onDone: () -> Unit) =
        viewModelScope.launch {
            pref.clearAll()
            maintenanceDao.deleteAll()
            onDone.invoke()
        }


    private fun getUserDetails() = viewModelScope.launch {
        useCase.getUserDetails(
            pref.getString(PrefKeys.USER_ID)
        ).onEach {
            _user.value = it
        }.launchIn(viewModelScope)
    }
}