package com.iitism.srijan24.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AuthRetrofitInstance {
    private const val BASE_URL = "https://srijan2024.onrender.com/" // Replace with your server URL

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
}