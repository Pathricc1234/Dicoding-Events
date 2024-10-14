package com.dicoding.dicodingevent.ui.reminder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingevent.database.EventsRepository
import com.dicoding.dicodingevent.database.Result
import com.dicoding.dicodingevent.database.local.entity.ReminderEventEntity
import com.dicoding.dicodingevent.database.remote.response.EventResponse
import kotlinx.coroutines.launch
import retrofit2.Response


class ReminderViewModel(private val eventsRepository: EventsRepository) : ViewModel() {
    private val notificationEvent = MutableLiveData<Result<EventResponse>>()

    init {
        getReminderEvent()
    }

    private fun getReminderEvent() = viewModelScope.launch {
        notificationEvent.postValue(Result.Loading)
        val response = eventsRepository.getReminderEvent()
        val event = response.body()?.listEvents?.get(0)
        val reminder = event?.let {
            ReminderEventEntity(
                it.id,
                event.name,
                event.beginTime
            )
        }
        viewModelScope.launch {
            if (reminder != null) {
                eventsRepository.insertReminderEvent(reminder)
            }
        }
        notificationEvent.postValue(handleActiveEventResponse(response))
    }

    private fun handleActiveEventResponse(response: Response<EventResponse>) : Result<EventResponse> {
        if(response.isSuccessful){
            response.body()?.let{ resultResponse ->
                return Result.Success(resultResponse)
            }
        }
        return Result.Error(response.message())
    }

    suspend fun setNotification(): ReminderEventEntity? {
        return eventsRepository.getReminderEventSync()
    }
}