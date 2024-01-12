package com.iitism.srijan24.data

import com.google.gson.annotations.SerializedName

data class GetTokensResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<Token>
)
