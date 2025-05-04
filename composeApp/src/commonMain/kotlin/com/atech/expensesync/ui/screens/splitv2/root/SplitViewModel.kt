package com.atech.expensesync.ui.screens.splitv2.root

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.expensesync.database.models.SplitFirebase
import com.atech.expensesync.database.models.User
import com.atech.expensesync.database.models.toGroupMember
import com.atech.expensesync.database.pref.PrefKeys
import com.atech.expensesync.database.pref.PrefManager
import com.atech.expensesync.firebase.usecase.SplitUseCases
import com.atech.expensesync.firebase.util.FirebaseResponse
import com.atech.expensesync.utils.fromJson
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID

class SplitViewModel(
    private val splitUseCases: SplitUseCases,
    private val pref: PrefManager
) : ViewModel() {


    private val _addSplitState = mutableStateOf(
        SplitFirebase(
            groupName = "",
            createdByUid = ""
        )
    )
    val addSplitState: State<SplitFirebase> get() = _addSplitState

    private val _allSplitGroups =
        mutableStateOf<FirebaseResponse<List<SplitFirebase>>>(FirebaseResponse.Loading)
    val allSplitGroups: State<FirebaseResponse<List<SplitFirebase>>> get() = _allSplitGroups

//    fun getAllMembers(groupId: String) = viewModelScope.async(Dispatchers.IO) {
//        splitV2UseCases.getSplitGroupMembers(groupId)
//    }.let {
//        runBlocking(Dispatchers.IO) {
//            it.await()
//        }
//    }


    fun onEvent(event: SplitV2Events) {
        when (event) {
            is SplitV2Events.OnAddSplitStateChange -> {
                _addSplitState.value = event.splitModel
            }

            SplitV2Events.ResetSplitState -> {
                _addSplitState.value = SplitFirebase(
                    groupName = "",
                    createdByUid = ""
                )
            }

            is SplitV2Events.LoadSplitGroups -> viewModelScope.launch {
//                val uid = pref.getString(PrefKeys.USER_ID)
//                splitUseCases.getSplitData.invoke(uid).onEach {
//                    _allSplitGroups.value = it
//                }.launchIn(viewModelScope)
            }

            SplitV2Events.SaveGroup -> viewModelScope.launch {
//                val userModel = pref.getString(PrefKeys.USER_MODEL).fromJson<User>()
//                val uid = UUID.randomUUID().toString()
//                addSplitState.value.let { model ->
//                    splitUseCases.createSplitGroup.invoke(
//                        model = model.copy(
//                            groupID = uid,
//                            createdByUid = userModel.uid,
//                            groupMembers = listOf(userModel.uid),
//                            members = listOf(
//                                userModel.toGroupMember(
//                                    groupId = uid
//                                )
//                            ),
//                        )
//                    )
//                }
//                onEvent(SplitV2Events.ResetSplitState)
            }
        }
    }

}
