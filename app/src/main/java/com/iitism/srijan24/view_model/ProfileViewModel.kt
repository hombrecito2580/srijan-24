package com.iitism.srijan24.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iitism.srijan24.data.ProfileDataModel
import com.iitism.srijan24.data.SignUpDataModel
import com.iitism.srijan24.retrofit.AuthRetrofitInstance
import com.iitism.srijan24.retrofit.ProfileRetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel: ViewModel() {
    fun getCredentials(id:String, statusCode: (Int) -> Unit, isSuccess: (Boolean) -> Unit){
        viewModelScope.launch {

            val call= ProfileRetrofitInstance.profileApi.getProfile(id)

            call.enqueue(object : Callback<ProfileDataModel> {
                override fun onResponse(call: Call<ProfileDataModel>, response: Response<ProfileDataModel>) {
                    if (response.isSuccessful) {
                        statusCode.invoke(response.code())

                    } else {
                        isSuccess.invoke(false)
                    }
                }

                override fun onFailure(call: Call<ProfileDataModel>, t: Throwable) {
                    Log.e("CreateAnnouncement", "Network request failed", t)
                    isSuccess.invoke(false)
                }
            })
        }
    }
}