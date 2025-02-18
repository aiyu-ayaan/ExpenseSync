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
interface ExpanseTransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: ExpanseTransactions)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactionSplits(transactionSplits: List<TransactionSplit>)

    @Query("SELECT COUNT(*) FROM EXPANSE_GROUP_MEMBERS WHERE groupId = :groupId")
    fun getGroupMembers(groupId: String): Int?


    @Query("SELECT * FROM EXPANSE_GROUP_MEMBERS WHERE groupId = :groupId")
    fun getGroupMembersList(
        groupId: String
    ): List<ExpanseGroupMembers>

    @Query("SELECT * FROM expanse_transactions WHERE groupId = :groupId ORDER BY createdAt DESC")
    fun getGroupTransactions(groupId: String): Flow<List<ExpanseTransactions>>

    @Query("SELECT * FROM expanse_transactions WHERE transactionId = :transactionId")
    suspend fun getTransactionById(transactionId: String): ExpanseTransactions?

    @Update
    suspend fun updateTransaction(transaction: ExpanseTransactions)

    @Delete
    suspend fun deleteTransaction(transaction: ExpanseTransactions)


    @Transaction
    suspend fun insertTransactionWithSplits(
        transaction: ExpanseTransactions,
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

    @Query("SELECT * FROM expanse_transactions WHERE groupId = :groupId ORDER BY createdAt DESC")
    fun getGroupTransactionsFlow(groupId: String): Flow<List<ExpanseTransactions>>


    @Query("SELECT * FROM expanse_transaction_split WHERE transactionId = :transactionId")
    fun getTransactionSplits(transactionId: String): List<TransactionSplit>

    @Query("SELECT * FROM expanse_group_members WHERE uid = :uid")
    fun getGroupMembersByUid(uid: String): List<ExpanseGroupMembers>


    suspend fun mapTransactionWithSplitAndThenUser(groupId: String):
            Flow<Map<ExpanseTransactions, List<Pair<TransactionSplit, ExpanseGroupMembers>>>> =
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