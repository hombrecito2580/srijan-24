package com.iitism.srijan24.view_model

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iitism.srijan24.adapter.EventsAdapter
import com.iitism.srijan24.data.CoreTeamDataModel
import com.iitism.srijan24.data.EventDataModel
import com.iitism.srijan24.retrofit.EventsRetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class EventsViewModel(private val context: Context):ViewModel() {

    private  var _eventList = mutableStateListOf<EventDataModel>()
    val eventList : List<EventDataModel>
        get() = _eventList
    fun getEvents(){
        viewModelScope.launch {

            val call = EventsRetrofitInstance.eventsApi.getAllEvents()
            call.enqueue(object : retrofit2.Callback<List<EventDataModel>> {
                override fun onResponse(
                    call: Call<List<EventDataModel>>,
                    response: Response<List<EventDataModel>>
                ) {
                    if (response.isSuccessful) {
                        _eventList.addAll(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<List<EventDataModel>>, t: Throwable) {
                    Toast.makeText(context, "Response failed", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }
}