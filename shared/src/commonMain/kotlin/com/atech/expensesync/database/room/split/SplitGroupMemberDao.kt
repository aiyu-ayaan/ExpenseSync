package com.atech.expensesync.database.room.split

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SplitGroupMemberDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: SplitGroupMembers)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMembers(data: List<SplitGroupMembers>)

    @Query("SELECT * FROM split_group_members WHERE groupId = :groupId")
    fun getGroupMembers(groupId: String): Flow<List<SplitGroupMembers>>

    @Delete
    suspend fun removeMember(member: SplitGroupMembers)
}