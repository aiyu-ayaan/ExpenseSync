package com.atech.expensesync.ui.compose.split

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.expensesync.database.room.split.SplitGroup
import com.atech.expensesync.usecases.SplitUseCases
import kotlinx.coroutines.launch

class SplitViewModel(
    private val useCases: SplitUseCases
) : ViewModel() {

    val splitGroups = useCases.getAllGroups.invoke()
    private val _createGroupState = mutableStateOf<CreateGroupScreenState>(CreateGroupScreenState())
    val createGroupState: State<CreateGroupScreenState> get() = _createGroupState


    fun onAddGroupEvent(event: CreateGroupEvent) {
        when (event) {
            is CreateGroupEvent.OnStateChange ->
                _createGroupState.value = event.state

            CreateGroupEvent.SaveGroup -> viewModelScope.launch {
                useCases.createNewGroup(
                    SplitGroup(
                        name = createGroupState.value.groupName,
                        type = createGroupState.value.groupType.label,
                        path = ""
                    )
                )
                _createGroupState.value = CreateGroupScreenState()
            }
        }
    }
}