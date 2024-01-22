package com.iitism.srijan24.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iitism.srijan24.R
import com.iitism.srijan24.data.GetUserEventsResponse
import com.iitism.srijan24.data.GetUserEventsResponseItem

class ProfileRVEventsAdapter(private var dataList: ArrayList<GetUserEventsResponseItem>): RecyclerView.Adapter<ProfileRVEventsAdapter.EventsViewHolder>() {
    inner class EventsViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvEventName: TextView = view.findViewById(R.id.tvEvent)
        val tvTeamName: TextView = view.findViewById(R.id.tvTeamName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.registered_events_card, parent, false)
        return EventsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        val currentData = dataList[position]
        Log.d("abcd", position.toString())

            holder.tvEventName.text = "Events: ${currentData.eventName}"
        if(currentData.teams[0].TeamName.isNullOrEmpty()){
            holder.tvTeamName.visibility=View.INVISIBLE
        }else
            holder.tvTeamName.text = "Team Name: ${currentData.teams[0].TeamName}"
    }
}