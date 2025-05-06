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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
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
    globalTransactions: List<TransactionGlobalModel> = emptyList(),
    onSettleUpClick: (debtorUid: String, creditorUid: String, amount: Double) -> Unit = { _, _, _ -> }
) {
    val dataStore = LocalDataStore.current
    val loggedUserUid = dataStore.getString(PrefKeys.USER_ID)

    // Calculate net balances
    val netBalances = calculateNetBalances(globalTransactions)

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

            if (netBalances.isEmpty()) {
                Text(
                    text = "No debts to settle",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                netBalances.forEach { (personPair, amount) ->
                    val (debtorUid, creditorUid) = personPair

                    // Only show transactions where there's an actual debt
                    if (amount > 0) {
                        val debtor = groupMembers.find { it.uid == debtorUid }
                        val creditor = groupMembers.find { it.uid == creditorUid }

                        // Personalize the display for the logged-in user
                        val debtorName = if (debtorUid == loggedUserUid) "You"
                        else debtor?.name ?: "Unknown"
                        val creditorName = if (creditorUid == loggedUserUid) "You"
                        else creditor?.name ?: "Unknown"

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
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
                                        text = "₹${amount.toInt()}",
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

                            // Add Settle Up button
                            // Only show settle up button if the logged-in user is involved
                            if (debtorUid == loggedUserUid || creditorUid == loggedUserUid) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    ElevatedButton(
                                        onClick = { onSettleUpClick(debtorUid, creditorUid, amount) },
                                        colors = ButtonDefaults.elevatedButtonColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = MaterialTheme.colorScheme.onPrimary
                                        ),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.padding(top = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Settle up",
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Settle Up",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontWeight = FontWeight.Medium
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        // Add divider between items
                        val isLastItem =
                            netBalances.filterValues { it > 0 }.keys.indexOf(personPair) ==
                                    netBalances.filterValues { it > 0 }.size - 1
                        if (!isLastItem) {
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

/**
 * Calculates net balances between users based on all transactions
 * Returns a map of (debtorUid to creditorUid) -> net amount
 */
private fun calculateNetBalances(transactions: List<TransactionGlobalModel>): Map<Pair<String, String>, Double> {
    val balanceMap = mutableMapOf<Pair<String, String>, Double>()

    // First, aggregate all transactions
    transactions.forEach { transaction ->
        val debtorUid = transaction.path
        val creditorUid = transaction.paidByUid

        if (debtorUid != creditorUid && transaction.totalOwe > 0) {
            // Create a standardized pair with debtor and creditor
            val key = Pair(debtorUid, creditorUid)
            balanceMap[key] = (balanceMap[key] ?: 0.0) + transaction.totalOwe
        }
    }

    // Now calculate net balances by comparing both directions
    val netBalances = mutableMapOf<Pair<String, String>, Double>()

    balanceMap.forEach { (personPair, amount) ->
        val (debtorUid, creditorUid) = personPair
        val reversePair = Pair(creditorUid, debtorUid)
        val reverseAmount = balanceMap[reversePair] ?: 0.0

        if (amount > reverseAmount) {
            // Only store the net debt in one direction
            netBalances[personPair] = amount - reverseAmount
        }
        // The reverse case will be handled when we process the reverse pair
    }

    return netBalances
}

@Composable
fun ProfilePicture(
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