package com.iitism.srijan24.data

import com.google.gson.annotations.SerializedName

data class PlansDataModel(
    @SerializedName("razorpay_order_id") var orderId: String = "",
    @SerializedName("razorpay_payment_id") var paymentId: String = "",
    @SerializedName("razorpay_signature") var signature: String = "",
    @SerializedName("pacakage") var plan: String = ""
)