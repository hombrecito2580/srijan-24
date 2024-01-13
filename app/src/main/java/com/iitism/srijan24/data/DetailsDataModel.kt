package com.iitism.srijan24.data

import com.google.gson.annotations.SerializedName

data class DetailsDataModel(
    @SerializedName("tshirtSize") var tShirtSize: String = "",
    @SerializedName("address") var address: String = "",
    @SerializedName("quantity") var quantity: String = "",
    @SerializedName("imageURL") var imageURL: String = ""
)