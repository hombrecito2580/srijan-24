package com.iitism.srijan24.data

import com.google.gson.annotations.SerializedName

data class Announcement(
    @SerializedName("_id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String,
    @SerializedName("timestamp") val timestamp: String
)
