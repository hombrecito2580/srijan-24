package com.iitism.srijan24.retrofit

import com.iitism.srijan24.data.Announcement
import com.iitism.srijan24.data.CreateAnnouncementModel
import com.iitism.srijan24.data.Token
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AnnouncementApi {
    @GET("/api/announcements")
    fun getAnnouncements(): Call<List<Announcement>>

    @POST("/api/announcements")
    fun createAnnouncement(@Body body: CreateAnnouncementModel): Call<Void>
}