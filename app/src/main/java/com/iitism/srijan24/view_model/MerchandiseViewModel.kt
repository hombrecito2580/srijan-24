package com.iitism.srijan24.view_model

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iitism.srijan24.data.DetailsDataModel
import com.iitism.srijan24.retrofit.ApiResponse
import com.iitism.srijan24.retrofit.MerchandiseRetrofitInstance
import com.iitism.srijan24.ui.MerchandiseFragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.IOException

class MerchandiseViewModel(application: Application) : AndroidViewModel(application) {
    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> get() = _showLoading
    fun uploadData(dataModel: DetailsDataModel, selectedImageUri: Uri, context: Context, token: String) {

        try {
            _showLoading.value = true
//
//            val name =
//                dataModel.name.toRequestBody("text/plain".toMediaTypeOrNull())
//            val admissionNumber =
//                dataModel.admissionNumber.toRequestBody("text/plain".toMediaTypeOrNull())
//            val mobileNumber =
//                dataModel.mobileNumber.toRequestBody("text/plain".toMediaTypeOrNull())
            val tShirtSize =
                dataModel.tShirtSize.toRequestBody("text/plain".toMediaTypeOrNull())
            val address =
                RequestBody.create("text/plain".toMediaTypeOrNull(), dataModel.address)
//            val roomNumber =
//                RequestBody.create("text/plain".toMediaTypeOrNull(), dataModel.roomNumber)
            val quantity =
                RequestBody.create("text/plain".toMediaTypeOrNull(), dataModel.quantity)
            Log.i("ImageUri", selectedImageUri.toString())

            val imageFile = File(
                MerchandiseFragment.MyFileHandler(context).getFilePathFromContentUri(
                    context,
                    selectedImageUri
                )!!
            )

            Log.i("Data", dataModel.toString())
            Log.i("file", imageFile.toString())

            val fileRequestBody = imageFile.asRequestBody("*/*".toMediaTypeOrNull())
            val filePart =
                MultipartBody.Part.createFormData("image", imageFile.name, fileRequestBody)
            val call = MerchandiseRetrofitInstance.createUserApi(token).uploadData(
                tShirtSize,
                address,
                quantity,
                filePart
            )
            call.enqueue(object : retrofit2.Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
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
                                Log.d("QQQQQQQQQQQQQQQQQQQQ", response.code().toString())
                                Toast.makeText(context, "Unexpected error occurred", Toast.LENGTH_SHORT).show()
                                _showLoading.value = false
                            }
                        }
                    }
//                    if (response.body() == null) {
//                        Toast.makeText(
//                            context,
//                            "Something went wrong!",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        _showLoading.value = false
//
//                    } else {

//                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.i("Tag", t.toString())
                    Toast.makeText(
                        context,
                        "Try again !!, It may happen first time",
                        Toast.LENGTH_SHORT
                    ).show()
                    _showLoading.value = false

                }
            })

        } catch (e: IOException) {
            Log.d("MerchandiseViewModel", e.toString())
            _showLoading.value = false
        }
    }


}
