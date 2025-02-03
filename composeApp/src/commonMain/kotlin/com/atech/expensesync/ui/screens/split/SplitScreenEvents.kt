package com.atech.expensesync.ui.screens.split

sealed interface CreateGroupEvent {
    data class OnStateChange(val state: CreateGroupScreenState) : CreateGroupEvent
    data object SaveGroup : CreateGroupEvent
}