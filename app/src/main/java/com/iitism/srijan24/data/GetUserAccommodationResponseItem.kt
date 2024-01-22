package com.iitism.srijan24.data

import com.google.gson.annotations.SerializedName

data class GetUserAccommodationResponseItem(
    @SerializedName("Email") val email: String,
    @SerializedName("Gender") val gender: String,
    @SerializedName("HostleName") val hostelName: String,
    @SerializedName("Idproof") val idProof: String,
    @SerializedName("IsAccomodation") val isAccommodation: Boolean,
    @SerializedName("OrderId") val orderId: String,
    @SerializedName("Pacakage") val packageName: String,
    @SerializedName("PaymentId") val paymentId: String,
    @SerializedName("__v") val v: Int,
    @SerializedName("_id") val id: String
)