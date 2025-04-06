package com.atech.expensesync.database.room.upload

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UploadDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertUploadModel(uploadModel: UploadModel)

    @Update
    suspend fun updateUploadModel(uploadModel: UploadModel)

    @Query("SELECT * FROM upload_table ORDER BY createdAt DESC")
    suspend fun getAll(): Flow<List<UploadModel>>

    @Query("SELECT * FROM upload_table WHERE isUpdated = false")
    suspend fun getAllUnUploadModel(): List<UploadModel>

    @Query("SELECT * FROM upload_table WHERE updatedType = :type AND isUpdated = 0 LIMIT 1")
    suspend fun getUnUploadModelByType(type: String): UploadModel?


}