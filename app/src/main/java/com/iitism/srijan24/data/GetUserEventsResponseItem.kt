package com.iitism.srijan24.data

import com.google.gson.annotations.SerializedName

data class GetUserEventsResponseItem(
    @SerializedName("EventName") val eventName: String,
    @SerializedName("Teams") val teams: List<Team>,
    @SerializedName("__v") val v: Int,
    @SerializedName("_id") val id: String
)