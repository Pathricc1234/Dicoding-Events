package com.dicoding.dicodingevent.database.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.dicodingevent.database.local.entity.DetailEventEntity
import com.dicoding.dicodingevent.database.local.entity.FavoriteEventEntity
import com.dicoding.dicodingevent.database.local.entity.FinishedEventEntity
import com.dicoding.dicodingevent.database.local.entity.ReminderEventEntity
import com.dicoding.dicodingevent.database.local.entity.UpcomingEventEntity

@Database(entities = [UpcomingEventEntity::class, FinishedEventEntity::class,FavoriteEventEntity::class,ReminderEventEntity::class ,DetailEventEntity::class], version = 1, exportSchema = false)
abstract  class EventsDatabase : RoomDatabase() {
    abstract fun UpcomingEventDao(): UpcomingEventDao
    abstract fun FinishedEventDao(): FinishedEventDao
    abstract fun FavoriteEventDao(): FavoriteEventDao
    abstract fun ReminderEventDao(): ReminderEventDao
    abstract fun DetailEventDao(): DetailEventDao

    companion object{
        @Volatile
        private var instance: EventsDatabase? = null

        fun getInstance(context: Context): EventsDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    EventsDatabase::class.java, "Events.db"
                ).build()
            }
    }
}