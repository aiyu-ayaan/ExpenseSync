package com.atech.expensesync.ui.screens.split.root

import com.atech.expensesync.ui.screens.split.root.compose.add_group.TypeWithImage


data class CreateGroupScreenState(
    val groupName: String = "",
    val groupTypeWithImage: TypeWithImage = TypeWithImage.None,
)