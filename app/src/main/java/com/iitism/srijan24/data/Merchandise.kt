package com.iitism.srijan24.data

import com.google.gson.annotations.SerializedName

data class Merchandise(
    @SerializedName("_id") val id: String,
    @SerializedName("address") val address: String,
    @SerializedName("approved") val approved: Boolean,
    @SerializedName("imageURL") val imageURL: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("tshirtSize") val tShirtSize: String
)