package com.atech.expensesync.ui.screens.splitv2.root

import com.atech.expensesync.database.room.splitv2.SplitModel

sealed interface SplitV2Events {
    data class OnAddSplitStateChange(
        val splitModel: SplitModel
    ) : SplitV2Events

    data object ResetSplitState : SplitV2Events

    data object SaveGroup : SplitV2Events
}