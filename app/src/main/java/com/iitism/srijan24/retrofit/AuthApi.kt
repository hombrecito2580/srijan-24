package com.iitism.srijan24.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/login")
    fun login(
        @Body email:String,
        @Body password:String
    ): Call<Void>

    @POST("/signup")
    fun signup(
        @Body email:String,
        @Body password:String
    ): Call<Void>
}