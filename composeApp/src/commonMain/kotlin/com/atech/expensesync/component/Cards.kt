package com.atech.expensesync.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.atech.expensesync.database.room.split.SplitGroup
import com.atech.expensesync.ui.screens.split.root.compose.add_group.TypeWithImage
import com.atech.expensesync.ui.theme.spacing

@Composable
fun DefaultCard(
    modifier: Modifier = Modifier,
    colors: CardColors = CardDefaults.cardColors(),
    content: @Composable (BoxScope.() -> Unit)
) {
    Card(
        modifier = modifier,
        colors = colors,
        content = {
            Box(
                modifier = Modifier
                    .padding(MaterialTheme.spacing.medium)
            ) {
                content()
            }
        },
    )
}

@Composable
fun GroupItems(
    modifier: Modifier = Modifier,
    model: SplitGroup,
    onClick: () -> Unit
) {
    val enum = TypeWithImage.entries.find { it.name == model.type } ?: TypeWithImage.None
    DefaultCard(
        modifier = modifier.fillMaxWidth()
            .clickable { onClick() },
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .padding(MaterialTheme.spacing.medium)
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))  // Added rounded corners
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        modifier = Modifier.align(Alignment.Center),  // Center the icon
                        imageVector = enum.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary  // Added icon tint for better contrast
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.medium)
                ) {
                    Text(
                        text = model.groupName,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1
                    )
//                    Text(
//                        text = model.path,
//                        style = MaterialTheme.typography.bodyMedium,
//                        maxLines = 1
//                    )
                    Text(
                        text = enum.label,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    )
}