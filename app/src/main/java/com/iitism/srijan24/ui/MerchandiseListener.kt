package com.iitism.srijan24.ui

interface MerchandiseListener {
    fun sendOrder(
        tShirtSize: String,
        address: String,
        quantity: String,
        orderId: String
    )
}