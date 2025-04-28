package com.atech.expensesync.ui.screens.splitv2.details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.atech.expensesync.database.room.splitv2.SplitModel
import com.atech.expensesync.usecases.SplitV2UseCases

class SplitDetailsViewModel(
    private val splitV2UseCases: SplitV2UseCases,
) : ViewModel() {

    private val _detailsModel = mutableStateOf<SplitModel?>(null)
    val detailsModel: State<SplitModel?> get() = _detailsModel

    fun onEvent(onEvent: SplitDetailsEvents) {
        when (onEvent) {
            is SplitDetailsEvents.SetSplitModel -> {
                _detailsModel.value = onEvent.splitModel
            }
        }
    }
}