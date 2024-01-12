package com.iitism.srijan24.data

import com.google.gson.annotations.SerializedName

data class OTPDataModel(
    @SerializedName("otp") val otp: String,
    @SerializedName("Email") val email: String
)
