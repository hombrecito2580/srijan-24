package com.iitism.srijan24.view_model

import androidx.lifecycle.ViewModel
import com.iitism.srijan24.data.OTPDataModel
import com.iitism.srijan24.data.OTPResponse
import com.iitism.srijan24.retrofit.AuthRetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OTPViewModel: ViewModel() {
    var responseBody: OTPResponse? = null

    fun verifyOTP(otp: String, email: String, status: (Int) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val request = OTPDataModel(otp, email)
            val call= AuthRetrofitInstance.authApi.verifyOTP(request)

            call.enqueue(object : Callback<OTPResponse> {
                override fun onResponse(call: Call<OTPResponse>, response: Response<OTPResponse>) {
                    if(response.isSuccessful) {
                        responseBody = response.body()
                    }
                    status.invoke(response.code())
                }

                override fun onFailure(call: Call<OTPResponse>, t: Throwable) {
                    status.invoke(1000)
                }
            })
        }
    }
}