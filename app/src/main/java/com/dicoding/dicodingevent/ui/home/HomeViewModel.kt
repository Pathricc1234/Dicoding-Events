package com.dicoding.dicodingevent.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingevent.database.EventsRepository
import com.dicoding.dicodingevent.database.Result
import com.dicoding.dicodingevent.database.local.entity.FinishedEventEntity
import com.dicoding.dicodingevent.database.local.entity.UpcomingEventEntity
import com.dicoding.dicodingevent.database.remote.response.EventResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel(private val eventsRepository: EventsRepository) : ViewModel() {

    val activeEvent = MutableLiveData<Result<EventResponse>>()

    val finishedEvents = MutableLiveData<Result<EventResponse>>()

    init {
        getActiveEvents()
        getFinishedEvents()
    }

    private fun getActiveEvents() = viewModelScope.launch {
        activeEvent.postValue(Result.Loading)
        try {
            val response = eventsRepository.getUpcomingEvents()
            val events = response.body()?.listEvents
            val eventsList = ArrayList<UpcomingEventEntity>()
            events?.forEach { event ->
                val upcoming = UpcomingEventEntity(
                    event.id,
                    event.name,
                    event.summary,
                    event.imageLogo
                )
                eventsList.add(upcoming)
            }
            eventsRepository.saveUpcomingEvent(eventsList)
            activeEvent.postValue(handleActiveEventResponse(response))
        }
        catch (e: Exception) {
            activeEvent.postValue(Result.Error("An error occurred: ${e.message}"))
        }
    }

    private fun getFinishedEvents() = viewModelScope.launch {
        finishedEvents.postValue(Result.Loading)
        try {
            val response = eventsRepository.getFinishedEvents()
            val events = response.body()?.listEvents
            val eventsList = ArrayList<FinishedEventEntity>()
            events?.forEach { event ->
                val finished = FinishedEventEntity(
                    event.id,
                    event.name,
                    event.summary,
                    event.imageLogo
                )
                eventsList.add(finished)
            }
            eventsRepository.saveFinishedEvents(eventsList)
            finishedEvents.postValue(handleActiveEventResponse(response))
        }
         catch (e: Exception) {
            finishedEvents.postValue(Result.Error("An error occurred: ${e.message}"))
        }
    }

    private fun handleActiveEventResponse(response: Response<EventResponse>) : Result<EventResponse> {
        if(response.isSuccessful){
            response.body()?.let{ resultResponse ->
                return Result.Success(resultResponse)
            }
        }
        return Result.Error(response.message())
    }

    fun getHomeUpcomingEvent()  = eventsRepository.getHomeUpcoming()

    fun getHomeFinishedEvent() = eventsRepository.getHomeFinished()
}