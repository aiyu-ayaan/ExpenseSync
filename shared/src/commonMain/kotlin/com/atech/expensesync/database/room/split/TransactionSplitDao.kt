package com.atech.expensesync.database.room.split

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionSplitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSplit(split: TransactionSplit)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSplits(splits: List<TransactionSplit>)

    @Query("SELECT * FROM split_transaction_split WHERE transactionId = :transactionId")
    fun getTransactionSplits(transactionId: String): Flow<List<TransactionSplit>>

    @Query("SELECT * FROM split_transaction_split WHERE memberKey = :memberUid")
    fun getMemberSplits(memberUid: String): Flow<List<TransactionSplit>>
}