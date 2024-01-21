package com.iitism.srijan24.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iitism.srijan24.data.EventTeamModel
import com.iitism.srijan24.retrofit.EventsRetrofitInstance
import com.iitism.srijan24.retrofit.RegistrationRetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class RegistrationViewModel : ViewModel() {
    fun registerData(teamData: EventTeamModel, token: String, statusCode: (Int) -> Unit) {
        viewModelScope.launch {
            val call = RegistrationRetrofitInstance.createRegistrationApi(token).register(teamData)
            call.enqueue(object : retrofit2.Callback<Void> {
                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    statusCode.invoke(response.code())
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    statusCode.invoke(1000)
                }

            })
        }
    }
}