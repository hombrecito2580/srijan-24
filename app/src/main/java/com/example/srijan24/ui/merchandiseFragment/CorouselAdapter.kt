package com.example.srijan24.ui.merchandiseFragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.srijan24.R

class CorouselAdapter(private val corouselDataList: Array<Int>) : RecyclerView.Adapter<CorouselAdapter.CarouselItemViewHolder>()
{
    class CarouselItemViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselItemViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.item_corousel,parent,false)
        return CarouselItemViewHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return corouselDataList.size
    }


    override fun onBindViewHolder(holder: CarouselItemViewHolder, position: Int) {
        val imageView = holder.itemView.findViewById<ImageView>(R.id.image_merchandise)
        Log.i("src",corouselDataList[position].toString())
        Glide.with(holder.itemView.context)
            .load(corouselDataList[position])
            .placeholder(R.drawable.ic_merchandise)
            .error(R.drawable.ic_merchandise)
            .into(imageView)
    }

}