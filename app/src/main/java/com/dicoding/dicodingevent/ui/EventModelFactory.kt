package com.dicoding.dicodingevent.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.dicodingevent.database.EventsRepository
import com.dicoding.dicodingevent.di.Injection
import com.dicoding.dicodingevent.ui.detail.DetailViewModel
import com.dicoding.dicodingevent.ui.favorite.FavoriteViewModel
import com.dicoding.dicodingevent.ui.finished.FinishedViewModel
import com.dicoding.dicodingevent.ui.home.HomeViewModel
import com.dicoding.dicodingevent.ui.reminder.ReminderViewModel
import com.dicoding.dicodingevent.ui.upcoming.UpcomingViewModel

class EventModelFactory private constructor(private val eventsRepository: EventsRepository) :
    ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(FinishedViewModel::class.java) -> FinishedViewModel(eventsRepository)
            modelClass.isAssignableFrom(UpcomingViewModel::class.java) -> UpcomingViewModel(eventsRepository)
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> DetailViewModel(eventsRepository)
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(eventsRepository)
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> FavoriteViewModel(eventsRepository)
            modelClass.isAssignableFrom(ReminderViewModel::class.java) -> ReminderViewModel(eventsRepository)
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        } as T
    }

    companion object{
        @Volatile
        private var instance: EventModelFactory? = null
        fun getInstance(context: Context): EventModelFactory =
            instance ?: synchronized(this) {
                instance ?: EventModelFactory(Injection.provideEventRepository(context))
            }.also { instance = it }
    }
}