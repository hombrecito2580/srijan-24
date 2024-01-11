package com.iitism.srijan24.data

import com.google.gson.annotations.SerializedName

data class AddTokenModel(
    @SerializedName("token") val token: String
)