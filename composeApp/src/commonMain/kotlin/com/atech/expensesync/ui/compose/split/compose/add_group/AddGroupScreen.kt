package com.atech.expensesync.ui.compose.split.compose.add_group

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.atech.expensesync.component.AppButton
import com.atech.expensesync.component.EditTextEnhance
import com.atech.expensesync.component.MainContainer
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
    modifier: Modifier = Modifier
) {
    var selectedType by remember { mutableStateOf(Type.None) }
//    TODO: Add State for the group name
    MainContainer(
        modifier = modifier,
        title = "Create a group",
        actions = {
            AppButton(
                text = "Create",
                onClick = {
//                    TODO: Create a group
                }
            )
        },
        onNavigationClick = {

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
                value = "Yoyo",
                onValueChange = {

                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.TwoTone.Group,
                        contentDescription = null
                    )
                },
                placeholder = "Group Name",
                clearIconClick = {
//                    TODO: Clear the group name
                }
            )
            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
            Text(
                text = "Select a type",
                style = MaterialTheme.typography.bodyMedium
            )
            FlowRow (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
            ){
                Type.entries.filter { it.label.isNotBlank() }.forEach { type ->
                    FilterChip(
                        selected = type == selectedType,
                        onClick = {
                            selectedType = if(type != selectedType)
                                type
                            else
                                Type.None
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