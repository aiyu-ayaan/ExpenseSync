package com.atech.expensesync.room.split

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SplitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(splitGroup: SplitGroup)

    @Update
    suspend fun update(splitGroup: SplitGroup)

    @Delete
    suspend fun delete(splitGroup: SplitGroup)

    @Query("SELECT * FROM split_group order by created desc")
    fun getAll(): Flow<List<SplitGroup>>

}