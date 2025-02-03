package com.atech.expensesync.ui.screens.split

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.expensesync.database.room.split.ExpanseGroup
import com.atech.expensesync.usecases.SplitUseCases
import kotlinx.coroutines.launch
import java.util.UUID

class SplitViewModel(
    private val useCases: SplitUseCases
) : ViewModel() {

    val splitGroups = useCases.getAllGroups.invoke()
    private val _createGroupState = mutableStateOf<CreateGroupScreenState>(CreateGroupScreenState())
    val createGroupState: State<CreateGroupScreenState> get() = _createGroupState


    fun onAddGroupEvent(event: CreateGroupEvent) {
        when (event) {
            is CreateGroupEvent.OnStateChange -> _createGroupState.value = event.state

            CreateGroupEvent.SaveGroup -> viewModelScope.launch {
                useCases.createNewGroup(
                    ExpanseGroup(
                        groupId = UUID.randomUUID().toString(),
                        groupName = _createGroupState.value.groupName,
                        type = _createGroupState.value.groupType.label,
                        createdByUid = UUID.randomUUID().toString(),
                    )
                )
                _createGroupState.value = CreateGroupScreenState()
            }
        }
    }
}