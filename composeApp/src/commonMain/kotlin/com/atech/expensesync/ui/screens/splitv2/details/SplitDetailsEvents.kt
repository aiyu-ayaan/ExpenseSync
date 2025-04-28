package com.atech.expensesync.ui.screens.splitv2.details

import com.atech.expensesync.database.room.splitv2.SplitModel
import com.atech.expensesync.database.room.splitv2.SplitTransactions

sealed interface SplitDetailsEvents {
    data class SetSplitModel(val splitModel: SplitModel) : SplitDetailsEvents

    data class SetSplitTransaction(val splitTransaction: SplitTransactions) : SplitDetailsEvents
    data class OnEditTransaction(val splitTransaction: SplitTransactions) : SplitDetailsEvents
    data object SaveTransaction : SplitDetailsEvents

    data object InsertNewGroupMember : SplitDetailsEvents
}