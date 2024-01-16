package com.iitism.srijan24.data

import com.google.gson.annotations.SerializedName

data class GetSignatureResponse(
    @SerializedName("signature") val signature: String
)
