package com.iitism.srijan24.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iitism.srijan24.data.LoginDataModel
import com.iitism.srijan24.data.LoginResponse
import com.iitism.srijan24.data.OTPResponse
import com.iitism.srijan24.retrofit.AuthRetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    var responseBody: LoginResponse? = null

    fun checkCredentials(
        dataModel: LoginDataModel,
        statusCode: (Int) -> Unit
    ) {
        viewModelScope.launch {

            val call = AuthRetrofitInstance.authApi.login(dataModel)

            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        if(response.code() == 200) {
                            responseBody = response.body()
                        }
                        statusCode.invoke(response.code())
                    } else {
                        statusCode.invoke(1000)

                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("CreateAnnouncement", "Network request failed", t)
                    statusCode.invoke(1000)
                }
            })
        }
    }
}