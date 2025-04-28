package com.atech.expensesync.database.room.splitv2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SplitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSplitGroup(
        splitGroup: SplitModel
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSplitGroups(
        splitGroups: List<SplitModel>,
    )

    @Update
    suspend fun updateSplitGroup(
        splitGroup: SplitModel,
    )

    @Query("SELECT * FROM splitGroup order by createdAt DESC")
    fun getSplitGroups(): Flow<List<SplitModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSplitGroupMember(
        splitGroupMember: GroupMembers,
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSplitGroupMembers(
        splitGroupMembers: List<GroupMembers>,
    )

    @Update
    suspend fun updateSplitGroupMember(
        splitGroupMember: GroupMembers,
    )

    @Query("SELECT * FROM splitGroupName WHERE groupId = :groupId order by addedAt DESC")
    fun getSplitGroupMembers(
        groupId: String,
    ): Flow<List<GroupMembers>>

    @Query("SELECT * FROM splitGroupName WHERE groupId = :groupId order by addedAt DESC")
    suspend fun getSplitGroupMembersInternal(
        groupId: String,
    ): List<GroupMembers>

    @Transaction
    suspend fun createSplitGroup(
        splitGroup: SplitModel,
        splitGroupMembers: List<GroupMembers>,
    ) {
        insertSplitGroup(splitGroup)
        insertSplitGroupMembers(splitGroupMembers)
    }

    @Transaction
    suspend fun createSplitGroup(
        splitGroup: SplitModel,
        splitGroupMembers: GroupMembers,
    ) {
        insertSplitGroup(splitGroup)
        insertSplitGroupMember(splitGroupMembers)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSplitGlobalTransactions(
        splitGlobalTransactions: SplitGlobalTransactions,
    )

    @Update
    suspend fun updateSplitGlobalTransactions(
        splitGlobalTransactions: SplitGlobalTransactions,
    )

    @Query("SELECT * FROM splitGlobalTransactions WHERE groupId = :groupId")
    suspend fun checkIfSplitGlobalTransactionExists(
        groupId: String,
    ): SplitGlobalTransactions?


    @Query("SELECT * FROM splitGlobalTransactions WHERE groupId = :groupId")
    fun getSplitGlobalTransaction(
        groupId: String,
    ): Flow<SplitGlobalTransactions>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSplitGlobalTransactions(
        splitGlobalTransactions: List<SplitGlobalTransactions>,
    )

    @Query("SELECT * FROM splitGlobalTransactions WHERE groupId = :groupId AND groupMemberUid = :groupMemberUid")
    suspend fun getMemberSplitGlobalTransaction(
        groupId: String,
        groupMemberUid: String,
    ): SplitGlobalTransactions?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSplitTransaction(
        splitTransactions: SplitTransactions,
    )


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSplitTransactions(
        splitTransactions: List<SplitTransactions>,
    )

    @Update
    suspend fun updateSplitTransaction(
        splitTransactions: SplitTransactions,
    )

    @Query("SELECT * FROM splitTransactions WHERE groupId = :groupId order by createdAt DESC")
    fun getSplitTransactions(
        groupId: String,
    ): Flow<List<SplitTransactions>>


    @Transaction
    suspend fun createSplitTransaction(
        splitTransactions: SplitTransactions,
        splitGlobalTransactions: SplitGlobalTransactions,
    ) {
        if (checkIfSplitGlobalTransactionExists(splitGlobalTransactions.groupId) == null) {
            insertSplitGlobalTransactions(splitGlobalTransactions)
        } else {
            updateSplitGlobalTransactions(splitGlobalTransactions)
        }
        insertSplitTransaction(splitTransactions)
    }

}