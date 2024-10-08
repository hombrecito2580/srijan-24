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
    fun uploadCredentials(dataModel: SignUpDataModel, statusCode: (Int) -> Unit){
        viewModelScope.launch {

            val call=AuthRetrofitInstance.authApi.signup(dataModel)

            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    statusCode.invoke(response.code())
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("CreateAnnouncement", "Network request failed", t)
                    statusCode.invoke(1000)
                }
            })
        }
    }
}