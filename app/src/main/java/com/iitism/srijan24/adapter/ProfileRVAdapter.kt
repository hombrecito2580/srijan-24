package com.iitism.srijan24.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iitism.srijan24.R
import com.iitism.srijan24.data.Merchandise
import kotlin.math.max
import kotlin.math.min

class ProfileRVAdapter(private var dataList: List<Merchandise>): RecyclerView.Adapter<ProfileRVAdapter.ProfileViewHolder>() {
    inner class ProfileViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvOrderNumber: TextView = view.findViewById(R.id.tvOrderNumber)
        val tvAddress: TextView = view.findViewById(R.id.tvAddress)
        val tvSize: TextView = view.findViewById(R.id.tvSize)
        val tvQuantity: TextView = view.findViewById(R.id.tvQuantity)
        val tvSpecification: TextView = view.findViewById(R.id.tvSpecification)
        val tvPaymentId: TextView = view.findViewById(R.id.tvPaymentId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_rv_profile, parent, false)
        return ProfileViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val currentData = dataList[position]

        holder.tvOrderNumber.text = "Order: "+(position+1).toString()
        holder.tvAddress.text = "Address: "+currentData.address
        holder.tvSize.text = "Size: "+currentData.tShirtSize
        holder.tvQuantity.text = "Quantity: "+currentData.quantity
        holder.tvSpecification.text = "Specification: "+currentData.type
        holder.tvPaymentId.text = "Payment ID: "+currentData.paymentId

        Log.d("order$position", "Order: "+(position+1).toString())
        Log.d("order$position", "Address: "+currentData.address)
        Log.d("order$position", "Size: "+currentData.quantity)
        Log.d("order$position", "Quantity: "+currentData.quantity)
        Log.d("order$position", "Specification: "+currentData.type)
    }

    fun setData(data: List<Merchandise>) {
        val sizeBefore = dataList.size
        dataList = data
        val sizeAfter = data.size
        notifyItemRangeChanged(0, min(sizeBefore, sizeAfter))
        notifyItemRangeInserted(min(sizeBefore, sizeAfter), max(sizeBefore, sizeAfter) - min(sizeBefore, sizeAfter))
        notifyItemRangeRemoved(max(sizeBefore, sizeAfter), max(sizeBefore, sizeAfter) - min(sizeBefore, sizeAfter))
    }
}