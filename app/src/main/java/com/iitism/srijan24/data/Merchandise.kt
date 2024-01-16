package com.iitism.srijan24.data

import com.google.gson.annotations.SerializedName

data class Merchandise(
    @SerializedName("_id") val id: String,
    @SerializedName("address") val address: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("tshirtSize") val tShirtSize: String,
    @SerializedName("orderID") val orderId: String,
    @SerializedName("paymentID") val paymentId: String,
    @SerializedName("type") val type: String
)