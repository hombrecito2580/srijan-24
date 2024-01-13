package com.iitism.srijan24.retrofit
import com.iitism.srijan24.data.DetailsDataModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MerchandiseApiService {
    @POST("api/purchase")
    fun uploadData(
        @Body body: DetailsDataModel
    ): Call<Void>
}
