package com.dicoding.dicodingevent.database.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.dicodingevent.database.local.entity.FavoriteEventEntity

@Dao
interface FavoriteEventDao {
    @Query("SELECT * FROM favorite")
    fun getFavorite(): LiveData<List<FavoriteEventEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(event: FavoriteEventEntity)

    @Query("DELETE FROM favorite WHERE id = :id")
    suspend fun deleteFavorite(id: Int)
}