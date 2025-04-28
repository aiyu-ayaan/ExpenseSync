package com.atech.expensesync.ui.screens.splitv2.root.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.atech.expensesync.component.AppButton
import com.atech.expensesync.component.EditTextEnhance
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.room.splitv2.SplitModel
import com.atech.expensesync.database.room.splitv2.Type
import com.atech.expensesync.ui.screens.splitv2.root.SplitV2Events
import com.atech.expensesync.ui.theme.spacing

enum class TypeWithImage(
    val label: String,
    val icon: ImageVector
) {
    None("", Icons.TwoTone.FilterNone),
    Home("Home", Icons.TwoTone.Home),
    Couple("Couple", Icons.TwoTone.LocalHotel),
    Trip("Trip", Icons.TwoTone.AirplanemodeActive),
    Other("Other", Icons.AutoMirrored.TwoTone.ListAlt)
}

fun TypeWithImage.toType(): Type =
    when (this) {
        TypeWithImage.Home -> Type.Home
        TypeWithImage.Couple -> Type.Couple
        TypeWithImage.Trip -> Type.Trip
        TypeWithImage.Other -> Type.Other
        else -> Type.None
    }


fun Type.toTypeWithImage(): TypeWithImage =
    when (this) {
        Type.Home -> TypeWithImage.Home
        Type.Couple -> TypeWithImage.Couple
        Type.Trip -> TypeWithImage.Trip
        Type.Other -> TypeWithImage.Other
        else -> TypeWithImage.None
    }


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddGroupScreen(
    modifier: Modifier = Modifier,
    state: SplitModel,
    onEvent: (SplitV2Events) -> Unit,
    onNavigateBack: () -> Unit
) {

    MainContainer(
        modifier = modifier,
        title = "Create a group",
        onNavigationClick = {
            onEvent.invoke(SplitV2Events.ResetSplitState)
            onNavigateBack.invoke()
        },
        actions = {
            AppButton(
                text = "Create",
                enable = state.groupName.isNotBlank() && state.groupType != Type.None,
                onClick = {
                    onEvent(
                        SplitV2Events.SaveGroup
                    )
                    onNavigateBack()
                }
            )
        }
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
                        SplitV2Events.OnAddSplitStateChange(
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
                        SplitV2Events.OnAddSplitStateChange(
                            state.copy(groupName = "")
                        )
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences
                )
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
                TypeWithImage.entries.filter { it.label.isNotBlank() }.forEach { type ->
                    FilterChip(
                        selected = type == state.groupType.toTypeWithImage(),
                        onClick = {
                            onEvent(
                                SplitV2Events.OnAddSplitStateChange(
                                    state.copy(
                                        groupType  =
                                            (if (state.groupType.toTypeWithImage() == type) TypeWithImage.None
                                            else type).toType()
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