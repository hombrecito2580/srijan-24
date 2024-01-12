package com.iitism.srijan24.retrofit

import com.iitism.srijan24.data.LoginDataModel
import com.iitism.srijan24.data.SignUpDataModel
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthApi {

    @POST("/login")
    fun login(
        @Body body: LoginDataModel
    ): Call<Void>

    @POST("/signup")
    fun signup(
        @Body body: SignUpDataModel
    ): Call<Void>
}