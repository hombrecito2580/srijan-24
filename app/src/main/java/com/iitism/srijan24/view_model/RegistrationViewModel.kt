package com.iitism.srijan24.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iitism.srijan24.data.EventRegistrationResponse
import com.iitism.srijan24.data.EventTeamModel
import com.iitism.srijan24.retrofit.RegistrationRetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationViewModel : ViewModel() {
    var message: String = ""
    var status: String = ""
    var errorMessage: String = ""

    fun registerData(teamData: EventTeamModel, token: String, statusCode: (Int) -> Unit) {
        viewModelScope.launch {
            val call = RegistrationRetrofitInstance.createRegistrationApi(token).register(teamData)
            call.enqueue(object : Callback<EventRegistrationResponse> {
                override fun onResponse(
                    call: Call<EventRegistrationResponse>,
                    response: Response<EventRegistrationResponse>,
                ) {
                    if (!response.isSuccessful)
                        errorMessage = response.message()
                    //errorMessage = apiResponse?.message.toString()

                    statusCode.invoke(response.code())
                }

                override fun onFailure(call: Call<EventRegistrationResponse>, t: Throwable) {
                    statusCode.invoke(1000)
                }

            })
        }
    }
}