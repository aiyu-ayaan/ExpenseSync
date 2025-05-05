package com.atech.expensesync.ui.screens.splitv2.details.compose.settleUp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.atech.expensesync.LocalDataStore
import com.atech.expensesync.database.models.GroupMember
import com.atech.expensesync.database.models.TransactionGlobalModel
import com.atech.expensesync.database.pref.PrefKeys


@Composable
fun GlobalTransactionItem(
    modifier: Modifier = Modifier,
    groupMembers: List<GroupMember> = emptyList(),
    globalTransactions: List<TransactionGlobalModel> = emptyList()
) {
    val dataStore = LocalDataStore.current
    val loggedUserUid = dataStore.getString(PrefKeys.USER_ID)

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Debts Summary",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (globalTransactions.isEmpty()) {
                Text(
                    text = "No debts to settle",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                globalTransactions.forEach { transaction ->
                    // The path in your model is the UID of the person who owes money
                    val debtorUid = transaction.path
                    val creditorUid = transaction.paidByUid

                    val debtor = groupMembers.find { it.uid == debtorUid }
                    val creditor = groupMembers.find { it.uid == creditorUid }

                    // Personalize the display for the logged-in user
                    val debtorName = if (debtorUid == loggedUserUid) "You"
                    else debtor?.name ?: "Unknown"
                    val creditorName = if (creditorUid == loggedUserUid) "You"
                    else creditor?.name ?: "Unknown"

                    // Skip transactions where the user owes themselves (shouldn't happen but just in case)
                    if (debtorUid != creditorUid && transaction.totalOwe > 0) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Profile picture for debtor (who owes)
                                ProfilePicture(
                                    profilePicUrl = debtor?.pic,
                                    name = debtor?.name ?: "Unknown",
                                    isCurrentUser = debtorUid == loggedUserUid,
                                    size = 32.dp
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = if (debtorUid == loggedUserUid)
                                        "You owe"
                                    else
                                        "$debtorName owes",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (debtorUid == loggedUserUid) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "₹${transaction.totalOwe.toInt()}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                // Arrow indicating direction
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ArrowForward,
                                    contentDescription = "owes to",
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                // Profile picture for creditor (who is owed)
                                ProfilePicture(
                                    profilePicUrl = creditor?.pic,
                                    name = creditor?.name ?: "Unknown",
                                    isCurrentUser = creditorUid == loggedUserUid,
                                    size = 32.dp
                                )
                            }
                        }

                        if (transaction != globalTransactions.last()) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 4.dp),
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfilePicture(
    profilePicUrl: String?,
    name: String,
    isCurrentUser: Boolean,
    size: Dp = 32.dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (!profilePicUrl.isNullOrEmpty()) {
            // Load image using AsyncImage (from Coil)
            AsyncImage(
                model = profilePicUrl,
                contentDescription = "$name's profile picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Fallback if no profile picture is available
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        if (isCurrentUser)
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)
                        else
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isCurrentUser) "Y" else name.take(1).uppercase(),
                    color = if (isCurrentUser)
                        MaterialTheme.colorScheme.tertiary
                    else
                        MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}


//@Composable
//fun GlobalTransactionItem(
//    modifier: Modifier = Modifier,
//    groupMembers: List<GroupMember> = emptyList(),
//    globalTransactions: List<TransactionGlobalModel> = emptyList()
//) {
//    val dataStore = LocalDataStore.current
//    val loggedUserUid = dataStore.getString(PrefKeys.USER_ID)
//
//    OutlinedCard(
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(8.dp),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surface,
//        ),
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        ) {
//            Text(
//                text = "Debts Summary",
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.padding(bottom = 12.dp)
//            )
//
//            Spacer(modifier = Modifier.height(4.dp))
//
//            if (globalTransactions.isEmpty()) {
//                Text(
//                    text = "No debts to settle",
//                    style = MaterialTheme.typography.bodyMedium
//                )
//            } else {
//                globalTransactions.forEach { transaction ->
//                    // The path in your model is the UID of the person who owes money
//                    val debtorUid = transaction.path
//                    val creditorUid = transaction.paidByUid
//
//                    // Personalize the display for the logged-in user
//                    val debtorName = if (debtorUid == loggedUserUid) "You"
//                    else groupMembers.find { it.uid == debtorUid }?.name ?: "Unknown"
//                    val creditorName = if (creditorUid == loggedUserUid) "You"
//                    else groupMembers.find { it.uid == creditorUid }?.name ?: "Unknown"
//
//                    // Skip transactions where the user owes themselves (shouldn't happen but just in case)
//                    if (debtorUid != creditorUid && transaction.totalOwe > 0) {
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(vertical = 8.dp),
//                            horizontalArrangement = Arrangement.SpaceBetween,
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                // Avatar for debtor (who owes)
//                                Box(
//                                    modifier = Modifier
//                                        .size(32.dp)
//                                        .background(
//                                            if (debtorUid == loggedUserUid)
//                                                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)
//                                            else
//                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
//                                            CircleShape
//                                        ),
//                                    contentAlignment = Alignment.Center
//                                ) {
//                                    Text(
//                                        text = if (debtorUid == loggedUserUid) "Y"
//                                        else (groupMembers.find { it.uid == debtorUid }?.name?.take(
//                                            1
//                                        ) ?: "?").uppercase(),
//                                        color = if (debtorUid == loggedUserUid)
//                                            MaterialTheme.colorScheme.tertiary
//                                        else
//                                            MaterialTheme.colorScheme.primary,
//                                        style = MaterialTheme.typography.bodySmall
//                                    )
//                                }
//
//                                Spacer(modifier = Modifier.width(8.dp))
//
//                                Text(
//                                    text = if (debtorUid == loggedUserUid)
//                                        "You owe"
//                                    else
//                                        "$debtorName owes",
//                                    style = MaterialTheme.typography.bodyMedium.copy(
//                                        fontWeight = if (debtorUid == loggedUserUid) FontWeight.Bold else FontWeight.Normal
//                                    )
//                                )
//                            }
//
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                Text(
//                                    text = "₹${transaction.totalOwe.toInt()}",
//                                    style = MaterialTheme.typography.bodyMedium.copy(
//                                        fontWeight = FontWeight.Bold,
//                                        color = MaterialTheme.colorScheme.error
//                                    )
//                                )
//
//                                Spacer(modifier = Modifier.width(8.dp))
//
//                                // Arrow indicating direction
//                                Icon(
//                                    imageVector = Icons.AutoMirrored.Default.ArrowForward,
//                                    contentDescription = "owes to",
//                                    modifier = Modifier.size(16.dp),
//                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
//                                )
//
//                                Spacer(modifier = Modifier.width(8.dp))
//
//                                // Avatar for creditor (who is owed)
//                                Box(
//                                    modifier = Modifier
//                                        .size(32.dp)
//                                        .background(
//                                            if (creditorUid == loggedUserUid)
//                                                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)
//                                            else
//                                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
//                                            CircleShape
//                                        ),
//                                    contentAlignment = Alignment.Center
//                                ) {
//                                    Text(
//                                        text = if (creditorUid == loggedUserUid) "Y"
//                                        else (groupMembers.find { it.uid == creditorUid }?.name?.take(
//                                            1
//                                        ) ?: "?").uppercase(),
//                                        color = if (creditorUid == loggedUserUid)
//                                            MaterialTheme.colorScheme.tertiary
//                                        else
//                                            MaterialTheme.colorScheme.secondary,
//                                        style = MaterialTheme.typography.bodySmall
//                                    )
//                                }
//                            }
//                        }
//
//                        if (transaction != globalTransactions.last()) {
//                            HorizontalDivider(
//                                modifier = Modifier.padding(vertical = 4.dp),
//                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}