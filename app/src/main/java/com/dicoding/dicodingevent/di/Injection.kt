package com.dicoding.dicodingevent.di

import android.content.Context
import com.dicoding.dicodingevent.database.EventsRepository
import com.dicoding.dicodingevent.database.local.room.EventsDatabase
import com.dicoding.dicodingevent.database.remote.retrofit.ApiConfig

object Injection {
    fun provideEventRepository(context: Context) : EventsRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventsDatabase.getInstance(context)
        val upcomingDao = database.UpcomingEventDao()
        val finishedDao = database.FinishedEventDao()
        val favoriteDao = database.FavoriteEventDao()
        val reminderDao = database.ReminderEventDao()
        val detailDao = database.DetailEventDao()
        return EventsRepository.getInstance(apiService, upcomingDao, finishedDao,favoriteDao, reminderDao, detailDao)
    }
}