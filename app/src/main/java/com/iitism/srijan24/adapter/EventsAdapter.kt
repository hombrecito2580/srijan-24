package com.iitism.srijan24.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iitism.srijan24.R
import com.iitism.srijan24.data.EventDataModel

class EventsAdapter(val eventsList:List<EventDataModel>,val context: Context?): RecyclerView.Adapter<EventsAdapter.EventsViewHolder>() {

    inner class EventsViewHolder(v: View):RecyclerView.ViewHolder(v){
        var image: ImageView =v.findViewById(R.id.event_img)
        var eventName: TextView =v.findViewById(R.id.tvEvent)
        var organiser: TextView =v.findViewById(R.id.tvOrganiser)
        var mode: TextView =v.findViewById(R.id.tv_mode)
        var prize: TextView =v.findViewById(R.id.tv_prize)
        val btnViewMore:Button = v.findViewById(R.id.btn_viewmore)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsAdapter.EventsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.events_card_view, parent, false)
        return EventsViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventsAdapter.EventsViewHolder, position: Int) {
        val currentEvent=eventsList[position]

//        holder.eventName.text=currentEvent.name
//        holder.organiser.text=currentEvent.organizer
//        holder.mode.text=currentEvent.mode
//        holder.prize.text=currentEvent.prizes
//
//        Glide.with(holder.itemView.context)
//            .load(currentEvent.posterMobile)
//            .placeholder(R.drawable.srijan_modified_logo)
//            .centerCrop()
//            .into(holder.image)
//
//        holder.btnViewMore.setOnClickListener{
//
//        }
    }

    override fun getItemCount(): Int {
        return eventsList.size
    }
}