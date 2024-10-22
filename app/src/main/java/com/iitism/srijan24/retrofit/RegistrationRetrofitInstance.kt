package com.iitism.srijan24.retrofit

import com.iitism.srijan24.utils.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RegistrationRetrofitInstance {
//    private const val BASE_URL = "https://srijan2024.onrender.com/" // Replace with your server URL
    private const val AUTH_HEADER = "Authorization"

    private fun createOkHttpClient(jwtToken: String?): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)

        if (!jwtToken.isNullOrBlank()) {
            val interceptor = Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader(AUTH_HEADER, "Bearer $jwtToken")
                    .build()
                chain.proceed(request)
            }
            clientBuilder.addInterceptor(interceptor)
        }

        return clientBuilder.build()
    }

    private fun createRetrofit(jwtToken: String?): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient(jwtToken))
            .build()
    }

    fun createRegistrationApi(jwtToken: String?): RegistrationApi {
        return createRetrofit(jwtToken).create(RegistrationApi::class.java)
    }
}