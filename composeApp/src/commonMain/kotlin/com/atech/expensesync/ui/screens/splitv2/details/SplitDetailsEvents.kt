package com.atech.expensesync.ui.screens.splitv2.details

import com.atech.expensesync.database.room.splitv2.SplitModel

sealed interface SplitDetailsEvents {
    data class SetSplitModel(val splitModel: SplitModel) : SplitDetailsEvents
}