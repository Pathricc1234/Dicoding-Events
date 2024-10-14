package com.dicoding.dicodingevent.database.remote.retrofit

import com.dicoding.dicodingevent.database.remote.response.DetailEventResponse
import com.dicoding.dicodingevent.database.remote.response.EventResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    suspend fun getEvents(
        @Query("active") active: Int,
        @Query("limit") limit: Int
    ): Response<EventResponse>

    @GET("events/{id}")
    suspend fun getDetailEvent(
        @Path("id") id: String
    ): Response<DetailEventResponse>
}