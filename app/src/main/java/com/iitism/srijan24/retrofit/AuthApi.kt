package com.iitism.srijan24.retrofit

import com.iitism.srijan24.data.LoginDataModel
import com.iitism.srijan24.data.LoginResponse
import com.iitism.srijan24.data.OTPDataModel
import com.iitism.srijan24.data.OTPResponse
import com.iitism.srijan24.data.SignUpDataModel
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthApi {

    @POST("/api/login")
    fun login(
        @Body body: LoginDataModel
    ): Call<LoginResponse>

    @POST("/api/signup")
    fun signup(
        @Body body: SignUpDataModel
    ): Call<Void>

    @POST("/api/signup/verify")
    fun verifyOTP(
        @Body body: OTPDataModel
    ): Call<OTPResponse>
}