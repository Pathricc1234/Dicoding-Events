package com.dicoding.dicodingevent.ui.favorite

import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.database.EventsRepository

class FavoriteViewModel(private val eventsRepository: EventsRepository) : ViewModel(){
    fun getFavoriteEvent() = eventsRepository.getFavoriteEvents()
}