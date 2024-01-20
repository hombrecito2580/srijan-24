package com.iitism.srijan24.data

import com.google.gson.annotations.SerializedName

class UserEventsModel(
    @SerializedName("Email") var email: String?=null,
    @SerializedName("EventsRegistered") var eventsRegistered: List<String>?=null
)