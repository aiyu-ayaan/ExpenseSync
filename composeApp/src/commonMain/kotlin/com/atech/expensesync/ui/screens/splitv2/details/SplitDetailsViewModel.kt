package com.atech.expensesync.ui.screens.splitv2.details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.expensesync.database.room.splitv2.GroupMembers
import com.atech.expensesync.database.room.splitv2.SplitModel
import com.atech.expensesync.database.room.splitv2.SplitTransactions
import com.atech.expensesync.usecases.SplitV2UseCases
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID

class SplitDetailsViewModel(
    private val splitV2UseCases: SplitV2UseCases,
) : ViewModel() {

    private val _detailsModel = mutableStateOf<SplitModel?>(null)
    val detailsModel: State<SplitModel?> get() = _detailsModel

    private val _addOrUpdateSplitTransaction = mutableStateOf(SplitTransactions("", 0.0, ""))
    val addOrUpdateSplitTransaction: State<SplitTransactions> get() = _addOrUpdateSplitTransaction

    private val _groupMembers = mutableStateOf<List<GroupMembers>>(emptyList())
    val groupMembers: State<List<GroupMembers>> get() = _groupMembers

    fun onEvent(onEvent: SplitDetailsEvents) {
        when (onEvent) {
            is SplitDetailsEvents.SetSplitModel -> viewModelScope.launch {
                _detailsModel.value = onEvent.splitModel
                splitV2UseCases.getSplitGroupMembers.invoke(
                    _detailsModel.value?.groupId ?: return@launch
                ).onEach {
                    _groupMembers.value = it
                }.launchIn(viewModelScope)

            }

            is SplitDetailsEvents.SetSplitTransaction -> {
                _addOrUpdateSplitTransaction.value = onEvent.splitTransaction
            }

            is SplitDetailsEvents.OnEditTransaction -> {
                _addOrUpdateSplitTransaction.value = onEvent.splitTransaction
            }

            SplitDetailsEvents.SaveTransaction -> viewModelScope.launch {
                if (_detailsModel.value == null) {
                    return@launch
                }
                val groupId = _detailsModel.value!!.groupId
                val transaction = _addOrUpdateSplitTransaction.value
                splitV2UseCases.createSplitTransaction(
                    groupId = groupId,
                    amount = transaction.amount,
                    paidByUid = transaction.paidByUid,
                    description = transaction.description,
                    createdAt = transaction.createdAt,
                )
                _addOrUpdateSplitTransaction.value = SplitTransactions("", 0.0, "")
            }

            SplitDetailsEvents.InsertNewGroupMember -> {
                if (_detailsModel.value == null) {
                    return
                }
                val groupId = _detailsModel.value!!.groupId
                insertDummyGroupMember(groupId)
            }
        }
    }

    //    Todo: Remove me after testing
    private fun insertDummyGroupMember(
        groupId: String,
    ) = viewModelScope.launch {
        val randomWord = ('a'..'z').toList().shuffled().subList(0, 5).joinToString("")
        splitV2UseCases.insertSplitGroupMember(
            GroupMembers(
                groupId = groupId,
                uid = UUID.randomUUID().toString(),
                name = "Test $randomWord",
                email = "test$randomWord@expensesync.com",
                pic = "https://picsum.photos/200"
            )
        )
    }
}