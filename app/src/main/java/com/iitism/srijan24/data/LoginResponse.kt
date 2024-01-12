package com.iitism.srijan24.data

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("UserId") val userId: String,
    @SerializedName("Token") val token: String
)
