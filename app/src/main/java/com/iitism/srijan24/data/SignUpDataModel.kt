package com.iitism.srijan24.data

import com.google.gson.annotations.SerializedName


data class SignUpDataModel(
    @SerializedName("IsISM") var isISM: Boolean? = null,
    @SerializedName("Name") var name: String? = null,
    @SerializedName("Email") var email: String? = null,
    @SerializedName("PhoneNumber") var contact: String? = null,
    @SerializedName("Password") var password: String? = null
)
