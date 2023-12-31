package com.example.srijan24.view_model

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.srijan24.data.DetailsDataModel
import com.example.srijan24.retrofit.ApiResponse
import com.example.srijan24.retrofit.NetworkService
import com.example.srijan24.ui.MerchandiseFragment
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
    private val networkService = NetworkService()
    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> get() = _showLoading
    fun uploadData(dataModel: DetailsDataModel, selectedImageUri: Uri, context: Context) {

        try {
            _showLoading.value = true

            val name =
                dataModel.name.toRequestBody("text/plain".toMediaTypeOrNull())
            val admissionNumber =
                dataModel.admissionNumber.toRequestBody("text/plain".toMediaTypeOrNull())
            val mobileNumber =
                dataModel.mobileNumber.toRequestBody("text/plain".toMediaTypeOrNull())
            val tShirtSize =
                dataModel.tShirtSize.toRequestBody("text/plain".toMediaTypeOrNull())
            val hostel =
                RequestBody.create("text/plain".toMediaTypeOrNull(), dataModel.hostel)
            val roomNumber =
                RequestBody.create("text/plain".toMediaTypeOrNull(), dataModel.roomNumber)
            val quantity =
                RequestBody.create("text/plain".toMediaTypeOrNull(), dataModel.quantity)
            Log.i("ImageUri", selectedImageUri.toString())

            val imageFile: File = File(
                MerchandiseFragment.MyFileHandler(context).getFilePathFromContentUri(
                    context,
                    selectedImageUri
                )!!
            )
//
            Log.i("Data", dataModel.toString())
            Log.i("file", imageFile.toString())


            val fileRequestBody = imageFile.asRequestBody("*/*".toMediaTypeOrNull())
            val filePart =
                MultipartBody.Part.createFormData("image", imageFile.name, fileRequestBody)
            val call = networkService.merchandiseApiService.uploadData(
                name,
                admissionNumber,
                mobileNumber,
                tShirtSize,
                hostel,
                roomNumber,
                quantity,
                filePart
            )
            call.enqueue(object : retrofit2.Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    Log.i("Tag", response.toString())
//                binding.loadingCard.visibility = View.INVISIBLE
//                binding.scrollViewMerchandise.visibility = View.VISIBLE

                    Log.i("response", response.body()?.message.toString())
                    if (response.body() == null) {
                        Toast.makeText(
                            context,
                            "Something went wrong!",
                            Toast.LENGTH_SHORT
                        ).show()
                        _showLoading.value = false

                    } else {
                        Toast.makeText(context, "Order is succesfully placed!!", Toast.LENGTH_SHORT)
                            .show()
                        _showLoading.value = false
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.i("Tag", t.toString())
//                binding.loadingCard.visibility = View.INVISIBLE
//                binding.scrollViewMerchandise.visibility = View.VISIBLE
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
