package com.iitism.srijan24.data

import com.google.gson.annotations.SerializedName

data class MakeOrderBody(
    @SerializedName("amount") val amount: Int
)
