package com.atech.expensesync.ui.screens.split.add

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.expensesync.database.pref.PrefKeys
import com.atech.expensesync.database.pref.PrefManager
import com.atech.expensesync.database.room.split.ExpenseGroupMembers
import com.atech.expensesync.database.room.split.ExpenseTransactions
import com.atech.expensesync.database.room.split.TransactionSplit
import com.atech.expensesync.navigation.ViewExpanseBookArgs
import com.atech.expensesync.usecases.ExpanseGroupMemberUseCases
import com.atech.expensesync.usecases.ExpenseTransactionUseCases
import com.atech.expensesync.utils.fromJson
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID

class AddExpenseViewModel(
    private val expanseGroupMemberUseCases: ExpanseGroupMemberUseCases,
    private val expenseTransactionUseCases: ExpenseTransactionUseCases,
    private val prefManager: PrefManager
) : ViewModel() {
    var viewExpanseBookArgs: ViewExpanseBookArgs? = null

    private val _viewExpenseBookState = mutableStateOf<ViewExpenseBookState>(ViewExpenseBookState())
    val viewExpenseBookState: State<ViewExpenseBookState> get() = _viewExpenseBookState

    private val _grpMembers = mutableStateOf<List<ExpenseGroupMembers>>(emptyList())
    val grpMembers: State<List<ExpenseGroupMembers>> get() = _grpMembers

    private val _getTransactionWithUser =
        mutableStateOf<Map<ExpenseTransactions, List<Pair<TransactionSplit, ExpenseGroupMembers>>>>(
            emptyMap()
        )
    val getTransactionWithUser: State<Map<ExpenseTransactions, List<Pair<TransactionSplit, ExpenseGroupMembers>>>>
        get() = _getTransactionWithUser

    private val _createExpenseState = mutableStateOf<CreateExpenseState>(
        CreateExpenseState.default(
            prefManager.getString(PrefKeys.USER_MODEL).fromJson() ?: error("User model not found")
        )
    )
    val createExpenseState: State<CreateExpenseState> get() = _createExpenseState

    fun onEvent(event: AddExpenseEvents) {
        when (event) {
            AddExpenseEvents.GetExpenseGroupMembers -> if (viewExpanseBookArgs != null) {
                expanseGroupMemberUseCases.getAll(viewExpanseBookArgs!!.grpId).onEach {
                    _grpMembers.value = it
                }.launchIn(viewModelScope)
            }


            is AddExpenseEvents.SetViewExpenseBookArgs -> {
                if (event.args != null && viewExpanseBookArgs == null) {
                    viewExpanseBookArgs = event.args
                    _viewExpenseBookState.value = _viewExpenseBookState.value.copy(
                        groupName = viewExpanseBookArgs?.grpName ?: "",
                        groupId = viewExpanseBookArgs?.grpId ?: ""
                    )
                    onEvent(AddExpenseEvents.GetExpenseGroupMembers)
//                    re-assign default value
                    populateCreateExpenseState()
//                    TODO: Remove me after testing
//                    insertDummyGroupMember(viewExpanseBookArgs!!.grpId)
                }
            }

            is AddExpenseEvents.OnCreateExpenseStateChange -> _createExpenseState.value =
                event.state

            AddExpenseEvents.OnCreateExpenseStateReset -> populateCreateExpenseState()
            is AddExpenseEvents.AddExpenseToGroup -> viewModelScope.launch {
                val model =
                    CreateExpenseStateToExpanseTransactionsMapper().mapFromEntity(createExpenseState.value)
                expenseTransactionUseCases.createNewTransaction(model)
                _createExpenseState.value = CreateExpenseState.default(
                    prefManager.getString(PrefKeys.USER_MODEL).fromJson()
                        ?: error("User model not found"), viewExpanseBookArgs!!.grpId
                )
                event.onComplete()
            }

            is AddExpenseEvents.SetTransactionForEdit -> {
                val model =
                    CreateExpenseStateToExpanseTransactionsMapper().mapToCreateExpenseState(
                        domainModel = event.model,
                        splitTo = grpMembers.value,
                    )
                _createExpenseState.value = model
            }

            AddExpenseEvents.LoadSettleUpScreen -> viewModelScope.launch {
                expenseTransactionUseCases.mapTransactionWithSplitAndThenUser(
                    viewExpanseBookArgs?.grpId ?: ""
                ).onEach {
                    _getTransactionWithUser.value = it
                }.launchIn(viewModelScope)
            }

            AddExpenseEvents.InsertNewGroupMember -> insertDummyGroupMember(viewExpanseBookArgs!!.grpId)
        }
    }

    private fun populateCreateExpenseState() {
        _createExpenseState.value = CreateExpenseState.default(
            prefManager.getString(PrefKeys.USER_MODEL).fromJson() ?: error("User model not found"),
            viewExpanseBookArgs!!.grpId
        )
    }


    //    Todo: Remove me after testing
    private fun insertDummyGroupMember(
        groupId: String,
    ) = viewModelScope.launch {
        val randomWord = ('a'..'z').toList().shuffled().subList(0, 5).joinToString("")
        expanseGroupMemberUseCases.insert(
            data = ExpenseGroupMembers(
                groupId = groupId,
                uid = UUID.randomUUID().toString(),
                name = "Test $randomWord",
                email = "test$randomWord@expensesync.com",
                pic = "https://picsum.photos/200"
            )
        )
    }
}