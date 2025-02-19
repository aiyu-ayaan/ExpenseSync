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
interface ExpanseGroupDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(data: ExpanseGroup)

    @Insert
    suspend fun insertMembers(data: List<ExpenseGroupMembers>)


    @Query("SELECT * FROM expanse_group WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getAllActiveGroups(): Flow<List<ExpanseGroup>>

    @Query("SELECT * FROM expanse_group WHERE groupId = :groupId")
    suspend fun getGroupById(groupId: String): ExpanseGroup?

    @Update
    suspend fun updateGroup(group: ExpanseGroup)

    @Delete
    suspend fun deleteGroup(group: ExpanseGroup)


    @Transaction
    suspend fun insertGroupWithMembers(
        groupName: ExpanseGroup,
        members: List<ExpenseGroupMembers>
    ) {
        insert(groupName)
        insertMembers(members)
    }
}