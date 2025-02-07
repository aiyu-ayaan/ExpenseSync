package com.atech.expensesync.ui.screens.split.root

sealed interface CreateGroupEvent {
    data class OnStateChange(val state: CreateGroupScreenState) : CreateGroupEvent
    data object SaveGroup : CreateGroupEvent
}