package com.atech.expensesync.ui.screens.splitv2.details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.expensesync.database.models.GroupMember
import com.atech.expensesync.database.models.SplitFirebase
import com.atech.expensesync.database.models.SplitTransaction
import com.atech.expensesync.database.models.SplitTransactionElement
import com.atech.expensesync.database.models.TransactionGlobalModel
import com.atech.expensesync.firebase.usecase.SplitUseCases
import com.atech.expensesync.firebase.util.FirebaseResponse
import com.atech.expensesync.firebase.util.getOrNull
import com.atech.expensesync.firebase.util.isSuccess
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SplitDetailsViewModel(
    private val splitUseCases: SplitUseCases
) : ViewModel() {
    private val _splitDetails =
        mutableStateOf<FirebaseResponse<SplitFirebase>>(FirebaseResponse.Loading)
    val splitDetails: State<FirebaseResponse<SplitFirebase>> get() = _splitDetails

    private val _splitTransaction = mutableStateOf(SplitTransaction())
    val splitTransaction: State<SplitTransaction> get() = _splitTransaction

    private val _groupMembers = mutableStateOf<List<GroupMember>>(emptyList())
    val groupMembers: State<List<GroupMember>> get() = _groupMembers

    private val _groupId = mutableStateOf("")


    private val _globalTransactionDetails =
        mutableStateOf<List<TransactionGlobalModel>>(emptyList())
    val globalTransactionDetails: State<List<TransactionGlobalModel>> get() = _globalTransactionDetails

    private val _splitTransactions = mutableStateOf<List<SplitTransaction>>(emptyList())
    val splitTransactions: State<List<SplitTransaction>> get() = _splitTransactions


    fun onEvent(event: SplitDetailsEvents) {
        when (event) {
            SplitDetailsEvents.InsertNewGroupMember -> {}
            is SplitDetailsEvents.SetGroupMember -> {
                _groupMembers.value = event.members
            }

            is SplitDetailsEvents.SaveTransaction -> viewModelScope.launch {
                val size = groupMembers.value.size
                val amount = splitTransaction.value.amount / size
                _splitTransaction.value = _splitTransaction.value.copy(
                    splitMembers = groupMembers.value.map {
                        SplitTransactionElement(
                            it,
                            amount = amount,
                        )
                    })
                event.onDone(
                    splitUseCases.createTransaction(
                        _groupId.value, _splitTransaction.value
                    )
                )
            }

            is SplitDetailsEvents.SetId -> viewModelScope.launch {
                _groupId.value = event.id
                splitUseCases.getSplitById.invoke(
                    event.id
                ).onEach {
                    _splitDetails.value = it
                }.launchIn(viewModelScope)

                splitUseCases.getGlobalTransaction(event.id).onEach {
                    if (it.isSuccess()) {
                        _globalTransactionDetails.value = it.getOrNull() ?: emptyList()
                    } else {
                        _globalTransactionDetails.value = emptyList()
                    }
                }.launchIn(viewModelScope)

                splitUseCases.getTransaction(event.id).onEach {
                    if (it.isSuccess()) {
                        _splitTransactions.value = it.getOrNull() ?: emptyList()
                    } else {
                        _splitTransactions.value = emptyList()
                    }
                }.launchIn(viewModelScope)
            }

            is SplitDetailsEvents.OnEditTransaction -> {
                _splitTransaction.value = event.splitTransaction
            }

            is SplitDetailsEvents.SetSplitTransaction -> {
                _splitTransaction.value = event.splitTransaction ?: SplitTransaction()
            }

            is SplitDetailsEvents.SettleUpClick -> viewModelScope.launch {
                event.onDone(
                    splitUseCases.createTransaction(
                        _groupId.value, event.transaction
                    )
                )
            }
        }
    }
}