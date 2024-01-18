package com.iitism.srijan24.retrofit

import com.iitism.srijan24.data.EventDataModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.SimpleTimeZone

interface EventsApi {
    @GET("/events/{zone}")
    fun getAllEvents(
        @Query("zone")
        zone: String
    ): Call<List<EventDataModel>>
}