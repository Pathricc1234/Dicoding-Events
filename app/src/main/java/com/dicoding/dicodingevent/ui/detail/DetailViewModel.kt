package com.dicoding.dicodingevent.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingevent.database.EventsRepository
import com.dicoding.dicodingevent.database.local.entity.DetailEventEntity
import com.dicoding.dicodingevent.database.remote.response.DetailEventResponse
import com.dicoding.dicodingevent.database.Result
import com.dicoding.dicodingevent.database.local.entity.FavoriteEventEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class DetailViewModel(private val eventsRepository: EventsRepository) : ViewModel() {

    val eventDetail = MutableLiveData<Result<DetailEventResponse>>()

    fun getDetailEvent(id: String) = viewModelScope.launch {
        eventDetail.postValue(Result.Loading)
        try {
            val response = eventsRepository.getDetailEvent(id)
            val detail = response.body()?.event
            val isFavorite = withContext(Dispatchers.IO) {
                eventsRepository.isFavorite(id)
            }
            val detailEntity = detail?.let {
                DetailEventEntity(
                    it.id,
                    detail.imageLogo,
                    detail.mediaCover,
                    detail.name,
                    detail.summary,
                    detail.ownerName,
                    detail.beginTime,
                    detail.quota,
                    detail.registrants,
                    detail.description,
                    detail.link,
                    isFavorite
                )
            }
            viewModelScope.launch {
                if (detailEntity != null) {
                    eventsRepository.insertDetailEvent(detailEntity)
                }
            }
            eventDetail.postValue(handleActiveEventResponse(response))
        }
        catch (e: Exception){
            eventDetail.postValue(Result.Error("An error occurred: ${e.message}"))
        }
    }

    private fun handleActiveEventResponse(response: Response<DetailEventResponse>) : Result<DetailEventResponse> {
        if(response.isSuccessful){
            response.body()?.let{ resultResponse ->
                return Result.Success(resultResponse)
            }
        }
        return Result.Error(response.message())
    }

    fun setDetailEvent(id: String) = eventsRepository.setDetailEvent(id)

    fun updateDetail(detail: DetailEventEntity) = viewModelScope.launch(Dispatchers.IO) {
        eventsRepository.updateDetail(detail)
    }

    fun setFavorite(detail: DetailEventEntity, isFavorite: Boolean) = viewModelScope.launch {
        if(isFavorite){
            val favorite = FavoriteEventEntity(
                detail.id,
                detail.name,
                detail.summary,
                detail.logo
            )
            eventsRepository.setFavoriteEvent(favorite)
        }
        else{
            eventsRepository.deleteFavoriteEvent(detail.id)
        }
    }
}