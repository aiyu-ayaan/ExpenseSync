package com.atech.expensesync.database.room.upload

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.atech.expensesync.utils.convertToDateFormat

enum class UpdateType {
    MEAL,
    EXPENSE,
    SPLIT
}

@Entity(tableName = "upload_table")
data class UploadModel(
    val updatedType: UpdateType,
    val isUpdated: Boolean = false,
    val updatedTime: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = false)
    val createdAt: Long = System.currentTimeMillis(),
){
    @get:Ignore
    val updateTimeFormat
        get() = updatedTime.convertToDateFormat()

    @get:Ignore
    val createdAtFormat
        get() = createdAt.convertToDateFormat()
}