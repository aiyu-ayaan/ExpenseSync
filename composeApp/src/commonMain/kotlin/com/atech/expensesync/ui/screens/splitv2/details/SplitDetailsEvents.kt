package com.atech.expensesync.ui.screens.splitv2.details

import com.atech.expensesync.database.models.GroupMember
import com.atech.expensesync.database.models.SplitTransaction


sealed interface SplitDetailsEvents {
    data class SetId(val id: String) : SplitDetailsEvents
    data class SetGroupMember(val members: List<GroupMember>) : SplitDetailsEvents
    data class SetSplitTransaction(val splitTransaction: SplitTransaction? = null) :
        SplitDetailsEvents

    data class OnEditTransaction(val splitTransaction: SplitTransaction) : SplitDetailsEvents
    data class SaveTransaction(val onDone: (Exception?) -> Unit) : SplitDetailsEvents

    data object InsertNewGroupMember : SplitDetailsEvents
}