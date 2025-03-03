package com.atech.expensesync.database.room.split

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.atech.expensesync.utils.takeUpToTwoDecimal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@Dao
interface SplitTransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: SplitTransactions)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactionSplits(transactionSplits: List<TransactionSplit>)

    @Query("SELECT COUNT(*) FROM SPLIT_GROUP_MEMBERS WHERE groupId = :groupId")
    suspend fun getGroupMembers(groupId: String): Int?


    @Query("SELECT * FROM SPLIT_GROUP_MEMBERS WHERE groupId = :groupId")
    suspend fun getGroupMembersList(
        groupId: String
    ): List<SplitGroupMembers>

    @Query("SELECT * FROM split_transactions WHERE groupId = :groupId ORDER BY createdAt DESC")
    fun getGroupTransactions(groupId: String): Flow<List<SplitTransactions>>

    @Query("SELECT * FROM split_transactions WHERE transactionId = :transactionId")
    suspend fun getTransactionById(transactionId: String): SplitTransactions?

    @Update
    suspend fun updateTransaction(transaction: SplitTransactions)

    @Delete
    suspend fun deleteTransaction(transaction: SplitTransactions)


    @Transaction
    suspend fun insertTransactionWithSplits(
        transaction: SplitTransactions,
        splitType: SplitType = SplitType.EQUAL,
    ) {
        insertTransaction(transaction)
        val members = getGroupMembers(transaction.groupId)
        if (members != null) {
            val splitAmount = (transaction.amount / members).takeUpToTwoDecimal()
            val splits = getGroupMembersList(
                transaction.groupId
            ).map {
                TransactionSplit(
                    transactionId = transaction.transactionId,
                    memberKey = it.key,
                    amount = splitAmount,
                    paidBy = transaction.paidByKey
                )
            }
            insertTransactionSplits(splits)
        }
    }

    @Query("SELECT * FROM split_transactions WHERE groupId = :groupId ORDER BY createdAt DESC")
    fun getGroupTransactionsFlow(groupId: String): Flow<List<SplitTransactions>>


    @Query("SELECT * FROM split_transaction_split WHERE transactionId = :transactionId")
    suspend fun getTransactionSplits(transactionId: String): List<TransactionSplit>

    @Query("SELECT * FROM split_group_members WHERE uid = :uid")
    suspend fun getGroupMembersByUid(uid: String): List<SplitGroupMembers>


    suspend fun mapTransactionWithSplitAndThenUser(groupId: String):
            Flow<Map<SplitTransactions, List<Pair<TransactionSplit, SplitGroupMembers>>>> =
        getGroupTransactionsFlow(groupId).map { transactions ->
            withContext(Dispatchers.IO) {
                transactions.associate { transaction ->
                    val splits = getTransactionSplits(transaction.transactionId)
                    val splitsWithMembers = splits.map { split ->
                        val groupMember =
                            getGroupMembersByUid(split.memberKey.split("$")[0]).firstOrNull()
                                ?: throw IllegalStateException("No group member found for uid: ${split.memberKey}")

                        Pair(split, groupMember)
                    }
                    transaction to splitsWithMembers
                }
            }
        }
}