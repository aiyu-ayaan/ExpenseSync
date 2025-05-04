package com.atech.expensesync.ui.screens.splitv2.root

import com.atech.expensesync.database.models.SplitFirebase

sealed interface SplitV2Events {
    data class OnAddSplitStateChange(
        val splitModel: SplitFirebase
    ) : SplitV2Events

    data object ResetSplitState : SplitV2Events

    data object SaveGroup : SplitV2Events

    data object LoadSplitGroups : SplitV2Events
}