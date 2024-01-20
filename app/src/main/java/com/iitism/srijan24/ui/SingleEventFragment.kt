package com.iitism.srijan24.ui

import EventDataModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.iitism.srijan24.R
import com.iitism.srijan24.adapter.EventsViewPagerAdapter
import com.iitism.srijan24.databinding.FragmentHomeBinding
import com.iitism.srijan24.databinding.FragmentSingleEventBinding

class SingleEventFragment : Fragment() {

    private var _binding: FragmentSingleEventBinding? = null
    private val binding get() = _binding!!
    private lateinit var tabLayout: TabLayout
    private lateinit var eventData:EventDataModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentSingleEventBinding.inflate(inflater)
        arguments?.let {
            eventData = it.getParcelable<EventDataModel>("eventData")!!
            // Use the received data as needed
        }
        tabLayout=binding.tabLayout
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout.tabGravity= TabLayout.GRAVITY_FILL
        val adapter= EventsViewPagerAdapter(requireContext(),childFragmentManager,tabLayout.tabCount,eventData)
        binding.viewPager.adapter=adapter
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPager.currentItem=tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

    }

}