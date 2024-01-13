package com.iitism.srijan24.retrofit
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MerchandiseApiService {
    @Headers(
        "Authorization: "
    )
    @Multipart
    @POST("api/purchase")
    fun uploadData(
        @Part("name") name: RequestBody,
        @Part("admissionNumber") admissionNumber: RequestBody,
        @Part("mobileNumber") mobileNumber: RequestBody,
        @Part("tshirtSize") tshirtSize: RequestBody,
        @Part("hostel") hostel: RequestBody,
        @Part("roomNumber") roomNumber: RequestBody,
        @Part("quantity") quantity: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<ApiResponse>
}
