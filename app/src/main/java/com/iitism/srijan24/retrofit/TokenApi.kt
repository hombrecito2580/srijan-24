package com.iitism.srijan24.retrofit

import com.iitism.srijan24.data.AddTokenModel
import com.iitism.srijan24.data.GetTokensResponse
import com.iitism.srijan24.data.Token
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TokenApi {
    @GET("/api/tokens")
    fun getTokens(): Call<GetTokensResponse>

    @POST("/api/tokens")
    fun addToken(@Body body: AddTokenModel): Call<Void>
}