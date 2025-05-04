package com.atech.expensesync.ui.screens.splitv2.details


sealed interface SplitDetailsEvents {
    data class SetId(val id : String) : SplitDetailsEvents
//
//    data class SetSplitTransaction(val splitTransaction: SplitTransactions) : SplitDetailsEvents
//    data class OnEditTransaction(val splitTransaction: SplitTransactions) : SplitDetailsEvents
    data object SaveTransaction : SplitDetailsEvents

    data object InsertNewGroupMember : SplitDetailsEvents
}