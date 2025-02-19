package com.atech.expensesync.ui.screens.split.root

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.expensesync.database.models.User
import com.atech.expensesync.database.pref.PrefKeys
import com.atech.expensesync.database.pref.PrefManager
import com.atech.expensesync.database.room.split.ExpanseGroup
import com.atech.expensesync.database.room.split.ExpenseGroupMembers
import com.atech.expensesync.usecases.SplitUseCases
import com.atech.expensesync.utils.fromJson
import kotlinx.coroutines.launch
import java.util.UUID

class SplitViewModel(
    private val useCases: SplitUseCases,
    private val pref: PrefManager
) : ViewModel() {

    val splitGroups = useCases.getAllGroups.invoke()
    private val _createGroupState = mutableStateOf<CreateGroupScreenState>(CreateGroupScreenState())
    val createGroupState: State<CreateGroupScreenState> get() = _createGroupState


    fun onAddGroupEvent(event: CreateGroupEvent) {
        when (event) {
            is CreateGroupEvent.OnStateChange -> _createGroupState.value = event.state

            CreateGroupEvent.SaveGroup -> viewModelScope.launch {
                val userModel = pref.getString(PrefKeys.USER_MODEL).fromJson<User>()
                val groupId = UUID.randomUUID().toString()
                useCases.createNewGroup(
                    ExpanseGroup(
                        groupId = groupId,
                        groupName = _createGroupState.value.groupName,
                        type = _createGroupState.value.groupType.label,
                        createdByUid = userModel.uid,
                    ),
                    listOf(
                        ExpenseGroupMembers(
                            uid = userModel.uid,
                            name = userModel.name,
                            email = userModel.email,
                            pic = userModel.photoUrl,
                            groupId = groupId
                        )
                    )
                )
                _createGroupState.value = CreateGroupScreenState()
            }
        }
    }
}