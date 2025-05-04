package com.atech.expensesync.ui.screens.splitv2.details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.expensesync.database.models.SplitFirebase
import com.atech.expensesync.firebase.usecase.SplitUseCases
import com.atech.expensesync.firebase.util.FirebaseResponse
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SplitDetailsViewModel(
    private val splitUseCases: SplitUseCases
) : ViewModel() {
    private val _splitDetails =
        mutableStateOf<FirebaseResponse<SplitFirebase>>(FirebaseResponse.Loading)
    val splitDetails: State<FirebaseResponse<SplitFirebase>> get() = _splitDetails


    fun onEvent(event: SplitDetailsEvents) {
        when (event) {
            SplitDetailsEvents.InsertNewGroupMember -> {}
            SplitDetailsEvents.SaveTransaction -> {}
            is SplitDetailsEvents.SetId -> viewModelScope.launch {
                splitUseCases.getSplitById.invoke(
                    event.id
                ).onEach {
                    _splitDetails.value = it
                }.launchIn(viewModelScope)
            }
        }
    }
}