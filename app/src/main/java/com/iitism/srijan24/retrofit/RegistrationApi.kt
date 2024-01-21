package com.iitism.srijan24.retrofit

import com.iitism.srijan24.data.EventTeamModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RegistrationApi {

    @POST("/api/events/register")
    fun register(
        @Body body: EventTeamModel
    ): Call<Void>
}