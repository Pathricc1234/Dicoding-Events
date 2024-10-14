package com.dicoding.dicodingevent.database.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.dicodingevent.database.local.entity.UpcomingEventEntity

@Dao
interface UpcomingEventDao {
    @Query("SELECT * FROM upcomingEvents ORDER BY id DESC")
    fun getActiveEvents(): LiveData<List<UpcomingEventEntity>>

    @Query("SELECT * FROM upcomingEvents ORDER BY id DESC LIMIT 5")
    fun getHomeUpcomingEvents(): LiveData<List<UpcomingEventEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvents(events: List<UpcomingEventEntity>)
}