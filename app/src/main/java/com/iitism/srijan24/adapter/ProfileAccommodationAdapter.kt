package com.iitism.srijan24.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iitism.srijan24.R
import com.iitism.srijan24.data.GetUserAccommodationResponse
import com.iitism.srijan24.data.GetUserAccommodationResponseItem

class ProfileAccommodationAdapter(private var dataList: ArrayList<GetUserAccommodationResponseItem>): RecyclerView.Adapter<ProfileAccommodationAdapter.AccommodationViewHolder>() {
    inner class AccommodationViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvEmail: TextView = view.findViewById(R.id.tvEmail)
        val tvGender: TextView = view.findViewById(R.id.tvGender)
        val tvHostelName: TextView = view.findViewById(R.id.tvHostelName)
        val tvIDProof: TextView = view.findViewById(R.id.tvIDProof)
        val tvAccommodation: TextView = view.findViewById(R.id.tvAccommodation)
        val tvPaymentId: TextView = view.findViewById(R.id.tvPaymentId)
        val tvPackage: TextView = view.findViewById(R.id.tvPackage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccommodationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_rv_profile_plan, parent, false)
        return AccommodationViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: AccommodationViewHolder, position: Int) {
        val currentData = dataList[position]
        Log.d("abcd", position.toString())

        holder.tvEmail.text = "Email: \n${currentData.email}"
        holder.tvGender.text = "Gender: ${currentData.gender}"
        holder.tvHostelName.text = "Hostel Name: ${currentData.hostelName}"
        holder.tvIDProof.text = "ID Proof: \n${currentData.idProof}"
        holder.tvAccommodation.text = "Accommodation: "
        holder.tvPaymentId.text = "Payment ID: \n${currentData.paymentId}"
        holder.tvPackage.text = "Package: ${currentData.packageName.uppercase()}"
    }
}