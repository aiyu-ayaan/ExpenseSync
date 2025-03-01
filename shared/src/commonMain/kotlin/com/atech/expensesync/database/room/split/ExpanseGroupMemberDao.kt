package com.atech.expensesync.database.room.split

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpanseGroupMemberDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: ExpenseGroupMembers)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMembers(data: List<ExpenseGroupMembers>)

    @Query("SELECT * FROM expanse_group_members WHERE groupId = :groupId")
    fun getGroupMembers(groupId: String): Flow<List<ExpenseGroupMembers>>

    @Delete
    suspend fun removeMember(member: ExpenseGroupMembers)
}