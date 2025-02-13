package com.atech.expensesync.database.room.split

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpanseTransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: ExpanseTransactions)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactionSplits(transactionSplits: List<TransactionSplit>)

    @Query("SELECT COUNT(*) FROM EXPANSE_GROUP_MEMBERS WHERE groupId = :groupId")
    fun getGroupMembers(groupId: String): Int?


    @Query("SELECT * FROM EXPANSE_GROUP_MEMBERS WHERE groupId = :groupId")
    fun getGroupMembersList(groupId: String): List<ExpanseGroupMembers>

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
            val splitAmount = transaction.amount / members
            val splits = getGroupMembersList(transaction.groupId).map {
                TransactionSplit(
                    transactionId = transaction.transactionId,
                    memberUId = it.key,
                    amount = splitAmount
                )
            }
            insertTransactionSplits(splits)
        }
    }

}