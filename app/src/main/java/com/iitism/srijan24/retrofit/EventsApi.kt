package com.iitism.srijan24.retrofit


import EventDataModel
import retrofit2.Call
import retrofit2.http.GET

interface EventsApi {
    @GET("/api/events")
    fun getAllEvents(
    ): Call<List<EventDataModel>>
}