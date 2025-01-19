package com.atech.expensesync.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.atech.expensesync.room.split.GroupMemberListTypeConverter
import com.atech.expensesync.room.split.SplitDao
import com.atech.expensesync.room.split.SplitGroup
import com.atech.expensesync.room.split.SplitMoneyListTypeConverter


@Database(
    entities = [SplitGroup::class],
    version = 1
)
@TypeConverters(
    GroupMemberListTypeConverter::class,
    SplitMoneyListTypeConverter::class
)
abstract class ExpenseSyncDatabase : RoomDatabase() {
    abstract fun splitGroupDao(): SplitDao
}