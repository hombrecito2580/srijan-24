package com.iitism.srijan24.data

data class CreateAccountModel(
    var name: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var password: String? = null,
    var isIsm: Boolean? = null
)