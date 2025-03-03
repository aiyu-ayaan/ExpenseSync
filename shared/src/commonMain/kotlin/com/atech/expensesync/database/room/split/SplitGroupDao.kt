package com.atech.expensesync.database.room.split

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface SplitGroupDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(data: SplitGroup)

    @Insert
    suspend fun insertMembers(data: List<SplitGroupMembers>)


    @Query("SELECT * FROM split_group WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getAllActiveGroups(): Flow<List<SplitGroup>>

    @Query("SELECT * FROM split_group WHERE groupId = :groupId")
    suspend fun getGroupById(groupId: String): SplitGroup?

    @Update
    suspend fun updateGroup(group: SplitGroup)

    @Delete
    suspend fun deleteGroup(group: SplitGroup)


    @Transaction
    suspend fun insertGroupWithMembers(
        groupName: SplitGroup,
        members: List<SplitGroupMembers>
    ) {
        insert(groupName)
        insertMembers(members)
    }
}