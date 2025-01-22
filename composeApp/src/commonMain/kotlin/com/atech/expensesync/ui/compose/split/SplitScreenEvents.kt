package com.atech.expensesync.ui.compose.split

sealed interface CreateGroupEvent {
    data class OnStateChange(val state: AddGroupScreenState) : CreateGroupEvent
    data object SaveGroup : CreateGroupEvent
}