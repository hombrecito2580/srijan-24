package com.iitism.srijan24.retrofit

import com.iitism.srijan24.data.EventRegistrationResponse
import com.iitism.srijan24.data.EventTeamModel
import com.iitism.srijan24.data.Notes
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RegistrationApi {

    @POST("/api/event/register")
    fun register(
        @Body body: EventTeamModel
    ): Call<EventRegistrationResponse>
}