package com.iitism.srijan24.retrofit

import com.iitism.srijan24.data.GetUserAccommodationResponse
import com.iitism.srijan24.data.GetUserAccommodationResponseItem
import com.iitism.srijan24.data.GetUserEventsResponse
import com.iitism.srijan24.data.GetUserResponse
import retrofit2.Call
import retrofit2.http.GET

interface UserApi {
    @GET("/api/getUser")
    fun getUser(): Call<GetUserResponse>

    @GET("/api/profile/eventsRegistered")
    fun getUserEvents(): Call<GetUserEventsResponse>

    @GET("/api/profile/userAccomodation")
    fun getUserAccommodation(): Call<ArrayList<GetUserAccommodationResponseItem>>
}