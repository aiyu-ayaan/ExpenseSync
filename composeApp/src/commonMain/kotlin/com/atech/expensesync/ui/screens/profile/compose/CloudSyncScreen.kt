package com.atech.expensesync.ui.screens.profile.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.atech.expensesync.component.MainContainer
import com.atech.expensesync.database.room.upload.UpdateType
import com.atech.expensesync.database.room.upload.UploadModel
import com.atech.expensesync.ui.theme.appGreen
import com.atech.expensesync.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CloudSyncScreen(
    modifier: Modifier = Modifier,
    state: List<UploadModel>,
    onNavigationClick: () -> Unit = {},
) {
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    MainContainer(
        modifier = modifier.nestedScroll(
            topAppBarScrollBehavior.nestedScrollConnection
        ),
        scrollBehavior = topAppBarScrollBehavior,
        onNavigationClick = onNavigationClick,
        title = "Cloud Sync",
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(state) {
                UploadItem(it)
            }
        }
    }
}

@Composable
fun UploadItem(
    upload: UploadModel,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header row with type and status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Type of update with icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val (icon, color, label) = when (upload.updatedType) {
                        UpdateType.MEAL -> Triple(
                            Icons.Default.Restaurant,
                            MaterialTheme.colorScheme.primary,
                            "Meal"
                        )

                        UpdateType.EXPENSE -> Triple(
                            Icons.Default.Receipt,
                            MaterialTheme.colorScheme.secondary,
                            "Expense"
                        )

                        UpdateType.SPLIT -> Triple(
                            Icons.Default.People,
                            MaterialTheme.colorScheme.tertiary,
                            "Split"
                        )
                    }

                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = color,
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                }

                // Status indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val (statusIcon, statusColor, statusText) = if (upload.isUpdated) {
                        Triple(
                            Icons.Default.Check,
                            MaterialTheme.colorScheme.appGreen,
                            "Uploaded"
                        )
                    } else {
                        Triple(
                            Icons.Default.Pending,
                            Color(0xFFFFA000), // Amber
                            "Pending"
                        )
                    }

                    Icon(
                        imageVector = statusIcon,
                        contentDescription = statusText,
                        tint = statusColor,
                        modifier = Modifier.size(18.dp)
                    )

                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = statusColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            // Timestamps
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TimestampRow(
                    label = "Created",
                    timestamp = upload.createdAtFormat
                )

                TimestampRow(
                    label = "Updated",
                    timestamp = upload.updateTimeFormat
                )
            }
        }
    }
}

@Composable
fun TimestampRow(
    label: String,
    timestamp: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = timestamp,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

