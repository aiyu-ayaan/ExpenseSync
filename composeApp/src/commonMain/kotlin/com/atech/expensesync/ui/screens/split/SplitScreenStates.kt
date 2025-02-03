package com.atech.expensesync.ui.screens.split

import com.atech.expensesync.ui.screens.split.compose.add_group.Type


data class CreateGroupScreenState(
    val groupName: String = "",
    val groupType: Type = Type.None,
)