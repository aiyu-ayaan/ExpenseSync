package com.atech.expensesync.ui.screens.split.compose.add_group

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ListAlt
import androidx.compose.material.icons.twotone.AirplanemodeActive
import androidx.compose.material.icons.twotone.FilterNone
import androidx.compose.material.icons.twotone.Group
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.LocalHotel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.atech.expensesync.component.AppButton
import com.atech.expensesync.component.EditTextEnhance
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.ui.screens.split.CreateGroupEvent
import com.atech.expensesync.ui.screens.split.CreateGroupScreenState
import com.atech.expensesync.ui.theme.spacing

enum class Type(
    val label: String,
    val icon: ImageVector
) {
    None("", Icons.TwoTone.FilterNone),
    Home("Home", Icons.TwoTone.Home),
    Couple("Couple", Icons.TwoTone.LocalHotel),
    Trip("Trip", Icons.TwoTone.AirplanemodeActive),
    Other("Other", Icons.AutoMirrored.TwoTone.ListAlt)
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddGroupScreen(
    modifier: Modifier = Modifier,
    state: CreateGroupScreenState,
    onEvent: (CreateGroupEvent) -> Unit,
    onNavigateBack: () -> Unit
) {

    MainContainer(
        modifier = modifier,
        title = "Create a group",
        actions = {
            AppButton(
                text = "Create",
                enable = state.groupName.isNotBlank() && state.groupType != Type.None,
                onClick = {
                    onEvent(
                        CreateGroupEvent.SaveGroup
                    )
                    onNavigateBack()
                }
            )
        },
        onNavigationClick = onNavigateBack
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.medium)
                .padding(contentPadding),
        ) {
            EditTextEnhance(
                modifier = Modifier.fillMaxWidth(),
                value = state.groupName,
                onValueChange = {
                    onEvent(
                        CreateGroupEvent.OnStateChange(
                            state.copy(groupName = it)
                        )
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.TwoTone.Group,
                        contentDescription = null
                    )
                },
                placeholder = "Group Name",
                clearIconClick = {
                    onEvent(
                        CreateGroupEvent.OnStateChange(
                            state.copy(groupName = "")
                        )
                    )
                }
            )
            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
            Text(
                text = "Select a type",
                style = MaterialTheme.typography.bodyMedium
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
            ) {
                Type.entries.filter { it.label.isNotBlank() }.forEach { type ->
                    FilterChip(
                        selected = type == state.groupType,
                        onClick = {
                            onEvent(
                                CreateGroupEvent.OnStateChange(
                                    state.copy(
                                        groupType =
                                            if (state.groupType == type) Type.None
                                            else type
                                    )
                                )
                            )
                        },
                        label = {
                            Text(type.label)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = type.icon,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        }
    }
}