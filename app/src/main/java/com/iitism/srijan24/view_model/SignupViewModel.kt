package com.iitism.srijan24.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iitism.srijan24.data.SignUpDataModel
import com.iitism.srijan24.retrofit.AuthRetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel: ViewModel() {
    fun uploadCredentials(dataModel: SignUpDataModel, statusCode: (Int) -> Unit, isSuccess: (Boolean) -> Unit){
        viewModelScope.launch {

            val call=AuthRetrofitInstance.authApi.signup(dataModel)

            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        statusCode.invoke(response.code())
                    } else {
                        isSuccess.invoke(false)
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("CreateAnnouncement", "Network request failed", t)
                    isSuccess.invoke(false)
                }
            })
        }
    }
}