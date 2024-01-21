package com.iitism.srijan24.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iitism.srijan24.R

class HomeCarouselAdapter(private val dataList: Array<String>): RecyclerView.Adapter<HomeCarouselAdapter.HomeCarouselViewHolder>() {
    inner class HomeCarouselViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCarouselViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.item_corousel,parent,false)
        return HomeCarouselViewHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: HomeCarouselViewHolder, position: Int) {
        val imageView = holder.itemView.findViewById<ImageView>(R.id.image_merchandise)
        Glide.with(holder.itemView.context)
            .load(dataList[position])
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.try_later)
            .centerCrop()
            .into(imageView)
    }
}