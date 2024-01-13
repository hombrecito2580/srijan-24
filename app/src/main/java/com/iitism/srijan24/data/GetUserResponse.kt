package com.iitism.srijan24.data

import com.google.gson.annotations.SerializedName

data class GetUserResponse(
    @SerializedName("Email") val email: String,
    @SerializedName("IsEvents") val isEvents: Boolean,
    @SerializedName("IsISM") val isISM: Boolean,
    @SerializedName("IsProNight") val isProNight: Boolean,
    @SerializedName("Merchandise") val merchandise: List<Any>,
    @SerializedName("Name") val name: String,
    @SerializedName("Password") val password: String,
    @SerializedName("PhoneNumber") val phoneNumber: String,
    @SerializedName("__v") val v: Int,
    @SerializedName("_id") val id: String,
    @SerializedName("otp") val otp: String,
    @SerializedName("otp_expiry_time") val otpExpiryTime: String,
    @SerializedName("verified") val verified: Boolean
)