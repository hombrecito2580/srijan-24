package com.iitism.srijan24.data

import com.google.gson.annotations.SerializedName

class LoginDataModel(
    @SerializedName("Email") var email:String?=null,
    @SerializedName("Password") var password:String?=null
)