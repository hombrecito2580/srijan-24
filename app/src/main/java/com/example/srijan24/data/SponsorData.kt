package com.example.srijan24.data

import com.google.gson.annotations.SerializedName

data class SponsorData (
    @SerializedName("img") val img: String,
    @SerializedName("link") val link: String
)