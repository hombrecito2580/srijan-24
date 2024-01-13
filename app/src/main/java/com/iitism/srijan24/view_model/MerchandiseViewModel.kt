package com.iitism.srijan24.view_model

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iitism.srijan24.data.DetailsDataModel
import com.iitism.srijan24.retrofit.MerchandiseRetrofitInstance
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

class MerchandiseViewModel(application: Application) : AndroidViewModel(application) {
    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> get() = _showLoading
    fun uploadData(dataModel: DetailsDataModel, context: Context, token: String) {

        try {
            _showLoading.value = true

            val call = MerchandiseRetrofitInstance.createUserApi(token).uploadData(dataModel)
            call.enqueue(object : retrofit2.Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(response.isSuccessful) {
                        Toast.makeText(context, "Order is successfully placed!!", Toast.LENGTH_SHORT)
                            .show()
                        _showLoading.value = false
                    } else {
                        when(response.code()) {
                            403 -> {
                                Toast.makeText(context, "Authorization Unsuccessful", Toast.LENGTH_SHORT).show()
                                _showLoading.value = false
                            }

                            404 -> {
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                                _showLoading.value = false
                            }

                            else -> {
                                Toast.makeText(context, "Unexpected error occurred", Toast.LENGTH_SHORT).show()
                                _showLoading.value = false
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.i("Tag", t.toString())
                    Toast.makeText(
                        context,
                        "Try again !!, It may happen first time",
                        Toast.LENGTH_SHORT
                    ).show()
                    _showLoading.value = false

                }
            }
        )

        } catch (e: IOException) {
            Log.d("MerchandiseViewModel", e.toString())
            _showLoading.value = false
        }
    }


}
