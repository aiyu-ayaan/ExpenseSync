package com.atech.expensesync.ui.screens.split.root

import com.atech.expensesync.ui.screens.split.root.compose.add_group.Type


data class CreateGroupScreenState(
    val groupName: String = "",
    val groupType: Type = Type.None,
)