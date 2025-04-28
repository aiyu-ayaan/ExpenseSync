package com.atech.expensesync.database.room.splitv2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction

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


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSplitGroupMember(
        splitGroupMember: GroupMembers,
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSplitGroupMembers(
        splitGroupMembers: List<GroupMembers>,
    )

    @Transaction
    suspend fun createExpenseGroup(
        splitGroup: SplitModel,
        splitGroupMembers: List<GroupMembers>,
    )  {
        insertSplitGroup(splitGroup)
        insertSplitGroupMembers(splitGroupMembers)
    }

    @Transaction
    suspend fun createExpenseGroup(
        splitGroup: SplitModel,
        splitGroupMembers: GroupMembers,
    ) {
        insertSplitGroup(splitGroup)
        insertSplitGroupMember(splitGroupMembers)
    }
}