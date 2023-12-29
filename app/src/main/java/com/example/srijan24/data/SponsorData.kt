package com.example.srijan24.data

import com.google.gson.annotations.SerializedName

data class SponsorData (
    @SerializedName("img") val image: String,
    @SerializedName("is_title") val isTitle: Boolean,
    @SerializedName("link") val redirectURL: String,
    @SerializedName("cat") val sponsorType: String
)