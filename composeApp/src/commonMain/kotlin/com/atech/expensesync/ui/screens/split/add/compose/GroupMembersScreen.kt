package com.atech.expensesync.ui.screens.split.add.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.GroupAdd
import androidx.compose.material.icons.twotone.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.atech.expensesync.LocalDataStore
import com.atech.expensesync.component.LoadImageFromUrl
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.pref.PrefKeys
import com.atech.expensesync.database.room.split.ExpenseGroupMembers
import com.atech.expensesync.ui.screens.split.add.AddExpenseEvents
import com.atech.expensesync.ui.theme.ExpenseSyncTheme
import com.atech.expensesync.ui.theme.spacing
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupMembersScreen(
    modifier: Modifier = Modifier,
    state: List<ExpenseGroupMembers> = emptyList(),
    onEvent: (AddExpenseEvents) -> Unit = {},
    onNavigationClick: () -> Unit = {},
) {
    val uid = LocalDataStore.current.getString(PrefKeys.USER_ID)
    MainContainer(
        title = "Group Members",
        modifier = modifier,
        onNavigationClick = onNavigationClick,
        actions = {
            IconButton(onClick = {
                onEvent.invoke(AddExpenseEvents.InsertNewGroupMember)
            }) {
                Icon(
                    imageVector = Icons.TwoTone.GroupAdd,
                    contentDescription = null
                )
            }
        }
    ) { paddingValue ->
        LazyColumn(
            modifier = Modifier,
            contentPadding = paddingValue
        ) {
            item(key = "title") {
                Text(
                    modifier = Modifier.padding(MaterialTheme.spacing.medium),
                    text = "Group Members",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            items(state) {
                UserItem(
                    groupMembers = it,
                    isOwner = it.uid == uid
                )
            }
        }
    }
}


@Composable
fun UserItem(
    modifier: Modifier = Modifier,
    groupMembers: ExpenseGroupMembers,
    isOwner: Boolean = false
) {
    Column {
        ListItem(
            modifier = modifier,
            headlineContent = {
                Text(groupMembers.name)
            },
            supportingContent = {
                Text(groupMembers.email)
            },
            leadingContent = {
                LoadImageFromUrl(
                    modifier = Modifier.size(40.dp),
                    url = groupMembers.pic
                )
            },
            overlineContent = if (isOwner) {
                {
                    Text("Admin")
                }
            } else null,
            trailingContent = if (isOwner.not()) {
                {
                    Icon(
                        imageVector = Icons.TwoTone.Remove,
                        contentDescription = null
                    )
                }
            } else null
        )
        HorizontalDivider()
    }
}

@Preview
@Composable
private fun GroupMemberScreenPreview(modifier: Modifier = Modifier) {
    ExpenseSyncTheme {
        GroupMembersScreen(
        )
    }
}