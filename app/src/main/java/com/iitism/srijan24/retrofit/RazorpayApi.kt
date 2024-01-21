package com.iitism.srijan24.retrofit

import com.iitism.srijan24.data.DetailsDataModel
import com.iitism.srijan24.data.GetSignatureModel
import com.iitism.srijan24.data.GetSignatureResponse
import com.iitism.srijan24.data.MakeOrderBody
import com.iitism.srijan24.data.MakeOrderResponse
import com.iitism.srijan24.data.PlansDataModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RazorpayApi {
    @POST("/api/order")
    fun makeOrder(@Body body: MakeOrderBody): Call<MakeOrderResponse>

    @POST("/api/order/validate")
    fun submitOrderDetails(@Body body: DetailsDataModel): Call<Void>

    @POST("/api/order/getSignature")
    fun getSignature(@Body body: GetSignatureModel): Call<GetSignatureResponse>

    @POST("/api/user/package")
    fun submitPlanDetails(@Body body: PlansDataModel): Call<Void>
}