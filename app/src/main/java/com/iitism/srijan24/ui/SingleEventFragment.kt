package com.iitism.srijan24.ui

import EventDataModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.iitism.srijan24.adapter.EventsViewPagerAdapter
import com.iitism.srijan24.databinding.FragmentSingleEventBinding
import com.iitism.srijan24.retrofit.EventsRetrofitInstance
import retrofit2.Call
import retrofit2.Response

class SingleEventFragment : Fragment() {

    private var _binding: FragmentSingleEventBinding? = null
    private val binding get() = _binding!!
    private lateinit var tabLayout: TabLayout
    private lateinit var eventData: EventDataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            eventData = it.getParcelable("eventData")!!
            // Use the received data as needed
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSingleEventBinding.inflate(inflater, container, false)

        tabLayout = binding.tabLayout
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val call = EventsRetrofitInstance.eventsApi.getAllEvents()
//        call.enqueue(object : retrofit2.Callback<List<EventDataModel>> {
//            override fun onResponse(
//                call: Call<List<EventDataModel>>,
//                response: Response<List<EventDataModel>>
//            ) {
//                if (response.isSuccessful) {
//                    _eventList.addAll(response.body()!!)
//                    eventList.value=_eventList.toList()
//                    isSuccess.invoke(true)
//                }else{
//                    isSuccess.invoke(false)
//                }
//            }
//
//            override fun onFailure(call: Call<List<EventDataModel>>, t: Throwable) {
//                isSuccess.invoke(false)
//            }
//
//        })

        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = EventsViewPagerAdapter(
            requireContext(),
            childFragmentManager,
            tabLayout.tabCount,
            eventData
        )
        binding.viewPager.adapter = adapter
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

    }

}