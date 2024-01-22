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

class EventsAdapter(private var eventsList: List<EventDataModel>, val context: Context?) :
    RecyclerView.Adapter<EventsAdapter.EventsViewHolder>(),
    Filterable {

    private var filteredEventsList: List<EventDataModel> = eventsList

    inner class EventsViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var image: ImageView = v.findViewById(R.id.event_img)
        var eventName: TextView = v.findViewById(R.id.tvEvent)
        var zone: TextView = v.findViewById(R.id.tvZone)
        var venue: TextView = v.findViewById(R.id.tv_venue)
        var prize: TextView = v.findViewById(R.id.tv_prize)
        val btnViewMore: Button = v.findViewById(R.id.btn_viewmore)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): EventsAdapter.EventsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.events_card_view, parent, false)
        return EventsViewHolder(view)
    }

    fun updateData(newList: List<EventDataModel>) {
        eventsList = newList
        filteredEventsList = newList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: EventsAdapter.EventsViewHolder, position: Int) {
        val currentEvent = filteredEventsList[position]

        holder.eventName.text = currentEvent.eventName
        holder.zone.text = currentEvent.zone
        holder.venue.text = currentEvent.venue
        holder.prize.text = currentEvent.venue

        val errorImage: Int = when (currentEvent.zone) {
            "Dance" -> {
                R.drawable.dance
            }

            "Music" -> {
                R.drawable.music
            }

            "Fashion" -> {
                R.drawable.fashion
            }

            "Finearts" -> {
                R.drawable.finearts
            }

            "Cinematography" -> {
                R.drawable.cinematography
            }

            "Dramatics" -> {
                R.drawable.drama
            }

            "Quiz" -> {
                R.drawable.quiz
            }

            "Literary" -> {
                R.drawable.literary
            }

            "Comedy" -> {
                R.drawable.comedy
            }

            else -> {
                R.drawable.culinary
            }
        }

        Glide.with(holder.itemView.context)
            .load(currentEvent.poster)
            .placeholder(R.drawable.progress_animation)
            .error(errorImage)
            .into(holder.image)

//        when(currentEvent.zone){
//            "Dance"->{
//                Glide.with(holder.itemView.context)
//                    .load(R.drawable.dance)
//                    .placeholder(R.drawable.srijan_modified_logo)
//                    .into(holder.image)
//            }
//            "Music"->{
//                Glide.with(holder.itemView.context)
//                    .load(R.drawable.music)
//                    .placeholder(R.drawable.srijan_modified_logo)
//                    .into(holder.image)
//            }
//            "Fashion"->{
//                Glide.with(holder.itemView.context)
//                    .load(R.drawable.fashion)
//                    .placeholder(R.drawable.srijan_modified_logo)
//                    .into(holder.image)
//            }
//            "Finearts"->{
//                Glide.with(holder.itemView.context)
//                    .load(R.drawable.finearts)
//                    .placeholder(R.drawable.srijan_modified_logo)
//                    .into(holder.image)
//            }
//            "Cinematography"->{
//                Glide.with(holder.itemView.context)
//                    .load(R.drawable.cinematography)
//                    .placeholder(R.drawable.srijan_modified_logo)
//                    .into(holder.image)
//            }
//            "Dramatics"->{
//                Glide.with(holder.itemView.context)
//                    .load(R.drawable.drama)
//                    .placeholder(R.drawable.srijan_modified_logo)
//                    .into(holder.image)
//            }
//            "Quiz"->{
//                Glide.with(holder.itemView.context)
//                    .load(R.drawable.quiz)
//                    .placeholder(R.drawable.srijan_modified_logo)
//                    .into(holder.image)
//            }
//            "Literary"->{
//                Glide.with(holder.itemView.context)
//                    .load(R.drawable.literary)
//                    .placeholder(R.drawable.srijan_modified_logo)
//                    .into(holder.image)
//            }
//            "Comedy"->{
//                Glide.with(holder.itemView.context)
//                    .load(R.drawable.comedy)
//                    .placeholder(R.drawable.srijan_modified_logo)
//                    .into(holder.image)
//            }
//            "Culinary"->{
//                Glide.with(holder.itemView.context)
//                    .load(R.drawable.culinary)
//                    .placeholder(R.drawable.srijan_modified_logo)
//                    .into(holder.image)
//            }
//        }


        holder.btnViewMore.setOnClickListener {

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