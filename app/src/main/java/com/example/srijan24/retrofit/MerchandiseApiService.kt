package com.example.srijan24.retrofit
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MerchandiseApiService {
    @Multipart
    @POST("api/purchase")
    fun uploadData(
        @Part("orderID") orderID: RequestBody,
        @Part("name") name: RequestBody,
        @Part("admissionNumber") admissionNumber: RequestBody,
        @Part("mobileNumber") mobileNumber: RequestBody,
        @Part("branch") branch: RequestBody,
        @Part("tshirtSize") tshirtSize: RequestBody,
        @Part("transactionID") transactionID: RequestBody,
        @Part("hostel") hostel: RequestBody,
        @Part("roomNumber") roomNumber: RequestBody,
        @Part("email") email : RequestBody,
        @Part image: MultipartBody.Part
    ): Call<ApiResponse>
}
