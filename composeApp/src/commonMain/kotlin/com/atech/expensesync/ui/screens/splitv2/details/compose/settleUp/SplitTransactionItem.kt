package com.atech.expensesync.ui.screens.splitv2.details.compose.settleUp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Money
import androidx.compose.material.icons.outlined.Percent
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.atech.expensesync.LocalDataStore
import com.atech.expensesync.database.models.SplitTransaction
import com.atech.expensesync.database.models.SplitType
import com.atech.expensesync.database.pref.PrefKeys
import com.atech.expensesync.utils.Currency
import com.atech.expensesync.utils.convertToDateFormat

@Composable
fun SplitTransactionItem(
    modifier: Modifier = Modifier,
    transaction: SplitTransaction,
    defaultCurrency: Currency = Currency.INR,
    onItemClick: (SplitTransaction) -> Unit = {}
) {
    val dateString = transaction.createdAt.convertToDateFormat()
    val currentUserUid = LocalDataStore.current.getString(PrefKeys.USER_ID)
    // Find who paid (created the transaction)
    val payer =
        transaction.splitMembers.find { it.groupMember.uid == transaction.createdByUid }?.groupMember

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        onClick = { onItemClick(transaction) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top section with description and amount
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = transaction.description,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "${defaultCurrency.symbol}${transaction.amount}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Date and split type info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dateString,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Split type chip
                SplitTypeChip(transaction.splitType)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Payer information with profile picture
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (payer != null) {
                    ProfilePicture(
                        profilePicUrl = payer.pic,
                        name = payer.name,
                        isCurrentUser = payer.uid == currentUserUid,
                        size = 40.dp
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = if (payer.uid == currentUserUid) "You paid" else "${payer.name} paid",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = "${defaultCurrency.symbol}${transaction.amount}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Settlement status
                if (transaction.settled) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Settled",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Members involved in split with mini avatars
            Column {
                Text(
                    text = "Split between",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy((-8).dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    transaction.splitMembers.take(5).forEach { member ->
                        ProfilePicture(
                            profilePicUrl = member.groupMember.pic,
                            name = member.groupMember.name,
                            isCurrentUser = member.groupMember.uid == currentUserUid,
                            size = 28.dp
                        )
                    }

                    if (transaction.splitMembers.size > 5) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Text(
                                text = "+${transaction.splitMembers.size - 5}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Your share information
                    val currentUserShare = transaction.splitMembers.find {
                        it.groupMember.uid == currentUserUid
                    }?.amount ?: 0.0

                    if (currentUserShare > 0) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Your share",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${defaultCurrency.symbol}${currentUserShare}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SplitTypeChip(splitType: SplitType) {
    val (icon, label, containerColor, contentColor) = when (splitType) {
        SplitType.EQUAL -> Quadruple(
            Icons.Outlined.Money,
            "Equal",
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer
        )

        SplitType.PERCENTAGE -> Quadruple(
            Icons.Outlined.Percent,
            "Percentage",
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer
        )
    }

    SuggestionChip(
        onClick = { /* No action needed */ },
        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = containerColor,
            labelColor = contentColor,
            iconContentColor = contentColor
        ),
        border = null
    )
}

// Helper data class for multiple return values
private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
