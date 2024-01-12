package com.iitism.srijan24.view_model

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.iitism.srijan24.data.LoginDataModel
import com.iitism.srijan24.retrofit.AuthRetrofitInstance
import kotlinx.coroutines.launch

class AuthenticationViewModel(application: Application) : AndroidViewModel(application) {



//    fun checkCredentials(dataModel: LoginDataModel, context: Context){
//        viewModelScope.launch {
//            AuthRetrofitInstance.authApi.signup(dataModel.email,dataModel.password)
//        }
//    }

}