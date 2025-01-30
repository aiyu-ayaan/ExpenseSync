package com.atech.expensesync.ui.compose.split

import com.atech.expensesync.ui.compose.split.compose.add_group.Type


data class CreateGroupScreenState(
    val groupName: String = "",
    val groupType: Type = Type.None,
)