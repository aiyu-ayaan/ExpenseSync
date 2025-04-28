package com.atech.expensesync.ui.screens.splitv2.root

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.expensesync.database.models.User
import com.atech.expensesync.database.pref.PrefKeys
import com.atech.expensesync.database.pref.PrefManager
import com.atech.expensesync.database.room.splitv2.GroupMembers
import com.atech.expensesync.database.room.splitv2.SplitModel
import com.atech.expensesync.usecases.SplitV2UseCases
import com.atech.expensesync.utils.fromJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SplitViewModel(
    private val splitV2UseCases: SplitV2UseCases,
    private val pref: PrefManager
) : ViewModel() {


    private val _addSplitState = mutableStateOf(
        SplitModel(
            groupName = "",
            createdByUid = ""
        )
    )
    val addSplitState: State<SplitModel> get() = _addSplitState

    val getAllSplitGroups = splitV2UseCases.getSplitGroups()

    fun getAllMembers(groupId: String) = viewModelScope.async(Dispatchers.IO) {
        splitV2UseCases.getSplitGroupMembers(groupId)
    }.let {
        runBlocking(Dispatchers.IO) {
            it.await()
        }
    }


    fun onEvent(event: SplitV2Events) {
        when (event) {
            is SplitV2Events.OnAddSplitStateChange -> {
                _addSplitState.value = event.splitModel
            }

            SplitV2Events.ResetSplitState -> {
                _addSplitState.value = SplitModel(
                    groupName = "",
                    createdByUid = ""
                )
            }

            SplitV2Events.SaveGroup -> viewModelScope.launch {
                val userModel = pref.getString(PrefKeys.USER_MODEL).fromJson<User>()
                _addSplitState.value.let { splitModel ->
                    splitV2UseCases.createSplitGroup.invoke(
                        splitModel = splitModel,
                        groupMembers = GroupMembers(
                            groupId = splitModel.groupId,
                            uid = userModel.uid,
                            name = userModel.name,
                            email = userModel.email,
                            pic = userModel.photoUrl,
                        )
                    )
                }
                onEvent(SplitV2Events.ResetSplitState)
            }
        }

    }
}