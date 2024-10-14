package com.dicoding.dicodingevent.database.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.dicodingevent.database.local.entity.ReminderEventEntity

@Dao
interface ReminderEventDao {
    @Query("SELECT * FROM reminder ORDER BY id DESC LIMIT 1")
    suspend fun getReminderEvent(): ReminderEventEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvents(events: ReminderEventEntity)
}