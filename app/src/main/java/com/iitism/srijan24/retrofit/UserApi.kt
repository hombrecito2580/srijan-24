package com.iitism.srijan24.retrofit

import com.iitism.srijan24.data.GetUserResponse
import retrofit2.Call
import retrofit2.http.GET

interface UserApi {
    @GET("/api/getUser")
    fun getUser(): Call<GetUserResponse>
}