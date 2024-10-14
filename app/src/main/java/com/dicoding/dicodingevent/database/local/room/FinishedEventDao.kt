package com.dicoding.dicodingevent.database.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.dicodingevent.database.local.entity.FinishedEventEntity

@Dao
interface FinishedEventDao {

    @Query("SELECT * FROM finishedEvents ORDER BY id DESC")
    fun getFinishedEvents(): LiveData<List<FinishedEventEntity>>

    @Query("SELECT * FROM finishedEvents ORDER BY id DESC LIMIT 5")
    fun getHomeFinished(): LiveData<List<FinishedEventEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvents(events: List<FinishedEventEntity>)
}