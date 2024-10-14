package com.dicoding.dicodingevent.ui.upcoming

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingevent.database.EventsRepository
import com.dicoding.dicodingevent.database.Result
import com.dicoding.dicodingevent.database.local.entity.UpcomingEventEntity
import com.dicoding.dicodingevent.database.remote.response.EventResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class UpcomingViewModel(private val eventsRepository: EventsRepository) : ViewModel() {

    val activeEvent = MutableLiveData<Result<EventResponse>>()

    init {
        getActiveEvents()
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
            viewModelScope.launch {
                eventsRepository.saveUpcomingEvent(eventsList)
            }
            activeEvent.postValue(handleActiveEventResponse(response))
        }
        catch (e: Exception){
            activeEvent.postValue(Result.Error("An error occurred: ${e.message}"))
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

    fun getAllUpcoming() = eventsRepository.getAllUpcoming()
}