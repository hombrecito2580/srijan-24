package com.iitism.srijan24.data

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("_id") val id: String,
    @SerializedName("token") val token: String
)
