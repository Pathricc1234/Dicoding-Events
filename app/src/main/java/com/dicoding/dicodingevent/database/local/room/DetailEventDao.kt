package com.dicoding.dicodingevent.database.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dicoding.dicodingevent.database.local.entity.DetailEventEntity

@Dao
interface DetailEventDao {
    @Query("SELECT * FROM detailevent WHERE id = :id")
    fun getDetailEvent(id: String): LiveData<DetailEventEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDetail(events: DetailEventEntity)

    @Update
    suspend fun updateDetail(detail: DetailEventEntity)

    @Query("SELECT EXISTS(SELECT * FROM favorite WHERE id = :id)")
    suspend fun isFavorite(id: String) : Boolean
}