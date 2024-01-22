package com.iitism.srijan24.adapter

import EventDataModel
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.iitism.srijan24.ui.AboutEventFragment
import com.iitism.srijan24.ui.EventDetailsFragment
import com.iitism.srijan24.ui.EventRulesFragment

internal class EventsViewPagerAdapter(var context: Context, fm: FragmentManager, private var tottabs: Int, private val eventData:EventDataModel):
    FragmentPagerAdapter(fm) {

    override fun getItem(position:Int): Fragment {
        val fragment: Fragment
        val bundle = Bundle().apply {
            putParcelable("eventData", eventData)
        }

        when (position) {
            0 -> {
                fragment = AboutEventFragment()
                fragment.arguments = bundle
            }
            1 -> {
                fragment = EventRulesFragment()
                fragment.arguments = bundle
            }
            else -> throw IllegalArgumentException("Invalid position: $position")
        }

        return fragment
    }

    override fun getCount(): Int {
        return tottabs
    }
}