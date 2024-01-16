package com.iitism.srijan24.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iitism.srijan24.R

class MerchandiseCarouselAdapter(private val carouselDataList: Array<String>) : RecyclerView.Adapter<MerchandiseCarouselAdapter.CarouselItemViewHolder>()
{
    inner class CarouselItemViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselItemViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.item_corousel,parent,false)
        return CarouselItemViewHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return carouselDataList.size
    }

    override fun onBindViewHolder(holder: CarouselItemViewHolder, position: Int) {
        val imageView = holder.itemView.findViewById<ImageView>(R.id.image_merchandise)
        Log.i("src", carouselDataList[position])
        Glide.with(holder.itemView.context)
            .load(carouselDataList[position])
            .placeholder(R.drawable.srijan_modified_logo)
            .error(R.drawable.srijan_modified_logo)
            .into(imageView)
    }

}