package com.iitism.srijan24.adapter

import EventDataModel
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iitism.srijan24.R

class EventsAdapter(private var eventsList:List<EventDataModel>, val context: Context?): RecyclerView.Adapter<EventsAdapter.EventsViewHolder>(),
    Filterable {

    private var filteredEventsList: List<EventDataModel> = eventsList

    inner class EventsViewHolder(v: View):RecyclerView.ViewHolder(v){
        var image: ImageView =v.findViewById(R.id.event_img)
        var eventName: TextView =v.findViewById(R.id.tvEvent)
        var zone: TextView =v.findViewById(R.id.tvZone)
        var venue: TextView =v.findViewById(R.id.tv_venue)
        var prize: TextView =v.findViewById(R.id.tv_prize)
        val btnViewMore:Button = v.findViewById(R.id.btn_viewmore)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsAdapter.EventsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.events_card_view, parent, false)
        return EventsViewHolder(view)
    }

    fun updateData(newList: List<EventDataModel>) {
        eventsList = newList
        filteredEventsList = newList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: EventsAdapter.EventsViewHolder, position: Int) {
        val currentEvent=filteredEventsList[position]

        holder.eventName.text=currentEvent.eventName
        holder.zone.text=currentEvent.zone
        holder.venue.text=currentEvent.venue
        holder.prize.text=currentEvent.venue

        when(currentEvent.zone){
            "Dance"->{
                Glide.with(holder.itemView.context)
                    .load("https://srijan-iitism.com/assets/Dance.05190f49712746c776b2-r0VBiceH.png")
                    .placeholder(R.drawable.srijan_modified_logo)
                    .into(holder.image)
            }
            "Music"->{
                Glide.with(holder.itemView.context)
                    .load("https://srijan-iitism.com/assets/Music.c91b700a4c5acdc4bb1c-pIhgd3tj.png")
                    .placeholder(R.drawable.srijan_modified_logo)
                    .into(holder.image)
            }
            "Fashion"->{
                Glide.with(holder.itemView.context)
                    .load("https://srijan-iitism.com/assets/Fashion.2b3060fc31ebc2867069-uxGrmxTl.png")
                    .placeholder(R.drawable.srijan_modified_logo)
                    .into(holder.image)
            }
            "Finearts"->{
                Glide.with(holder.itemView.context)
                    .load("https://srijan-iitism.com/assets/FineArts.6b8125d2615ce4bed692-dbxiu6I_.png")
                    .placeholder(R.drawable.srijan_modified_logo)
                    .into(holder.image)
            }
            "Cinematography"->{
                Glide.with(holder.itemView.context)
                    .load("https://srijan-iitism.com/assets/FilmFest.0aa5ff627365b4ef7d2f-9cFp181_.png")
                    .placeholder(R.drawable.srijan_modified_logo)
                    .into(holder.image)
            }
            "Dramatics"->{
                Glide.with(holder.itemView.context)
                    .load("https://srijan-iitism.com/assets/Drama.6fbebe13b5a463eababb-zotS868I.png")
                    .placeholder(R.drawable.srijan_modified_logo)
                    .into(holder.image)
            }
            "Quiz"->{
                Glide.with(holder.itemView.context)
                    .load("https://srijan-iitism.com/assets/download-MMAcZzpv.png")
                    .placeholder(R.drawable.srijan_modified_logo)
                    .into(holder.image)
            }
            "Literary"->{
                Glide.with(holder.itemView.context)
                    .load("https://srijan-iitism.com/assets/Literary.ee3a4fdc591b333cb436-LmHnZZtD.png")
                    .placeholder(R.drawable.srijan_modified_logo)
                    .into(holder.image)
            }
            "Comedy"->{
                Glide.with(holder.itemView.context)
                    .load("https://srijan-iitism.com/assets/pngegg-bJzZu3vP.png")
                    .placeholder(R.drawable.srijan_modified_logo)
                    .into(holder.image)
            }
            "Culinary"->{
                Glide.with(holder.itemView.context)
                    .load("https://srijan-iitism.com/assets/culinary-MoVQzFqO.png")
                    .placeholder(R.drawable.srijan_modified_logo)
                    .into(holder.image)
            }
        }


        holder.btnViewMore.setOnClickListener{

            val bundle = Bundle().apply {
                putParcelable("eventData", currentEvent)
            }
            findNavController(holder.itemView).popBackStack()
            findNavController(holder.itemView).navigate(R.id.singleEventFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return filteredEventsList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                val searchText = constraint?.toString()?.lowercase()

                if (searchText.isNullOrBlank()) {
                    filterResults.values = eventsList
                } else {
                    val filteredItems = eventsList.filter { item ->
                        item.eventName!!.lowercase().contains(searchText)
                    }
                    filterResults.values = filteredItems
                }

                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredEventsList = results?.values as List<EventDataModel>
                notifyDataSetChanged()
            }
        }
    }


//    override fun getFilter(): Filter {
//        return object : Filter() {
//            override fun performFiltering(constraint: CharSequence?): FilterResults {
//                val filteredList = mutableListOf<EventDataModel>()
//
//                if (constraint.isNullOrBlank()) {
//                    filteredList.addAll(eventsList)
//                } else {
//                    val filterPattern = constraint.toString().lowercase().trim()
//                    for (event in eventsList) {
//                        if (event.eventName!!.lowercase().contains(filterPattern)) {
//                            filteredList.add(event)
//                        }
//                    }
//                }
//
//                val results = FilterResults()
//                results.values = filteredList
//                return results
//            }
//
//            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//                filteredEventsList = results?.values as List<EventDataModel>
//                notifyDataSetChanged()
//            }
//        }
//    }
}