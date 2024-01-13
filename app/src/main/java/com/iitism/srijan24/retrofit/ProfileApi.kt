package com.iitism.srijan24.retrofit

import com.iitism.srijan24.data.ProfileDataModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ProfileApi {

    @GET("/profile")
    fun getProfile(
        @Query("id")
        id:String
    ):Call<ProfileDataModel>
}