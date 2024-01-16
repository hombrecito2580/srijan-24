package com.iitism.srijan24.retrofit

import com.iitism.srijan24.data.EventDataModel
import retrofit2.Call
import retrofit2.http.GET

interface EventsApi {
    @GET("/showAllEvents")
    fun getAllEvents(): Call<List<EventDataModel>>
}