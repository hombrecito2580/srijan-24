package com.iitism.srijan24.view_model

import EventDataModel
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iitism.srijan24.retrofit.EventsRetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class EventsViewModel():ViewModel() {

    private  var _eventList = mutableStateListOf<EventDataModel>()
    val eventList = MutableLiveData<List<EventDataModel>>()
    fun getEvents(isSuccess: (Boolean) -> Unit){
        viewModelScope.launch {

            val call = EventsRetrofitInstance.eventsApi.getAllEvents()
            call.enqueue(object : retrofit2.Callback<List<EventDataModel>> {
                override fun onResponse(
                    call: Call<List<EventDataModel>>,
                    response: Response<List<EventDataModel>>
                ) {
                    if (response.isSuccessful) {
                        _eventList.addAll(response.body()!!)
                        eventList.value=_eventList.toList()
                        isSuccess.invoke(true)
                    }else{
                        isSuccess.invoke(false)
                    }
                }

                override fun onFailure(call: Call<List<EventDataModel>>, t: Throwable) {
                    isSuccess.invoke(false)
                }

            })
        }
    }
}