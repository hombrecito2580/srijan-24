package com.iitism.srijan24.data

import com.google.gson.annotations.SerializedName

data class DetailsDataModel(
    @SerializedName("tshirtSize") var tShirtSize: String = "",
    @SerializedName("addresss") var address: String = "",
    @SerializedName("quantity") var quantity: String = "",
    @SerializedName("razorpay_order_id") var orderId: String = "",
    @SerializedName("razorpay_payment_id") var paymentId: String = "",
    @SerializedName("razorpay_signature") var signature: String = "",
    @SerializedName("type") var type: String = ""
)