package com.atech.expensesync.ui.screens.splitv2.root.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AccountCircle
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.atech.expensesync.database.models.GroupMember
import com.atech.expensesync.database.models.SplitFirebase
import com.atech.expensesync.ui.theme.spacing

@Composable
fun SplitGroupItem(
    splitModel: SplitFirebase,
    members: List<GroupMember>,
    onItemClick: (SplitFirebase) -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        onClick = { onItemClick(splitModel) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                    imageVector = splitModel.groupType.toTypeWithImage().icon,
                    contentDescription = "Group Icon",
                    modifier = Modifier.align(Alignment.Center),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = splitModel.groupName,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Show member count
                Text(
                    text = "${members.size} members",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                // Display currency
                Text(
                    text = splitModel.defaultCurrency.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

            }
        }

        if (members.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                val displayMembers = if (members.size > 5) members.take(4) else members

                displayMembers.forEach { member ->
                    Box(
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        SubcomposeAsyncImage(
                            model = member.pic,
                            contentDescription = "${member.name} avatar",
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            error = {
                                Icon(Icons.TwoTone.AccountCircle, null)
                            }
                        )
                    }
                }

                if (members.size > 5) {
                    Surface(
                        modifier = Modifier.size(24.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+${members.size - 4}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

