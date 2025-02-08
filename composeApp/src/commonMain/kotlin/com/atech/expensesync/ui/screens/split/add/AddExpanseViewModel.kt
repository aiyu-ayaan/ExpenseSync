package com.atech.expensesync.ui.screens.split.add

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.expensesync.database.room.split.ExpanseGroupMembers
import com.atech.expensesync.navigation.ViewExpanseBookArgs
import com.atech.expensesync.usecases.ExpanseGroupMemberUseCases
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AddExpanseViewModel(
    private val expanseGroupMemberUseCases: ExpanseGroupMemberUseCases
) : ViewModel() {
    var viewExpanseBookArgs: ViewExpanseBookArgs? = null

    private val _addExpenseState = mutableStateOf<AddExpenseState>(AddExpenseState())
    val addExpenseState: State<AddExpenseState> get() = _addExpenseState

    private val _grpMembers = mutableStateOf<List<ExpanseGroupMembers>>(emptyList())
    val grpMembers: State<List<ExpanseGroupMembers>> get() = _grpMembers

    fun onEvent(event: AddExpanseEvents) {
        when (event) {
            AddExpanseEvents.GetExpanseGroupMembers -> {
                if (viewExpanseBookArgs != null) {
                    expanseGroupMemberUseCases
                        .getAll(viewExpanseBookArgs!!.grpId)
                        .onEach {
                            _grpMembers.value = it
                        }.launchIn(viewModelScope)
                }
            }

            is AddExpanseEvents.SetViewExpanseBookArgs -> {
                if (event.args != null && viewExpanseBookArgs == null) {
                    com.atech.expensesync.utils.expenseSyncLogger("ViewExpanseBookArgs: called")
                    viewExpanseBookArgs = event.args
                    _addExpenseState.value = _addExpenseState.value.copy(
                        groupName = viewExpanseBookArgs?.grpName ?: "",
                        groupId = viewExpanseBookArgs?.grpId ?: ""
                    )
                }
            }
        }
    }
}