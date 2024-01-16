package com.iitism.srijan24.data

import com.google.gson.annotations.SerializedName

data class GetSignatureModel(
    @SerializedName("orderId") val orderID: String,
    @SerializedName("paymentId") val paymentId: String
)
