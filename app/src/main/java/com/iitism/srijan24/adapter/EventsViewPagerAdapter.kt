package com.iitism.srijan24.adapter

import EventDataModel
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.iitism.srijan24.ui.AboutEventFragment
import com.iitism.srijan24.ui.EventDetailsFragment
import com.iitism.srijan24.ui.EventRulesFragment

internal class EventsViewPagerAdapter(var context: Context, fm: FragmentManager, var tottabs: Int, val eventData:EventDataModel):
    FragmentPagerAdapter(fm) {

    override fun getItem(position:Int): Fragment {
        return when(position){
            0 ->{
                AboutEventFragment(eventData)
            }
            1 ->{
                EventRulesFragment(eventData)
            }
            else->
                getItem(position)
        }
    }

    override fun getCount(): Int {
        return tottabs
    }
}