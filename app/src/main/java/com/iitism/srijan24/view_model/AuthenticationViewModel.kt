package com.iitism.srijan24.view_model

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.iitism.srijan24.data.AuthDataModel
import com.iitism.srijan24.retrofit.AuthRetrofitInstance
import kotlinx.coroutines.launch

class AuthenticationViewModel(application: Application) : AndroidViewModel(application) {

    fun uploadCredentials(dataModel: AuthDataModel,context: Context){
        viewModelScope.launch {
            AuthRetrofitInstance.authApi.login(dataModel.email,dataModel.password)
        }
    }

    fun checkCredentials(dataModel: AuthDataModel,context: Context){
        viewModelScope.launch {
            AuthRetrofitInstance.authApi.signup(dataModel.email,dataModel.password)
        }
    }

}