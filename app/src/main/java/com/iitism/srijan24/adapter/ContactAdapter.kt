package com.iitism.srijan24.adapter

import ContactDataModel
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.iitism.srijan24.R

class ContactAdapter(val contactList:List<ContactDataModel>,val context: Context?): RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(v: View):RecyclerView.ViewHolder(v){
        var name: TextView =v.findViewById(R.id.tvContactName)
        var phone: TextView =v.findViewById(R.id.tvContactNumber)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactAdapter.ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_details_card, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactAdapter.ContactViewHolder, position: Int) {
        val contact=contactList[position]
        holder.name.text=contact.name
        holder.phone.text=contact.phoneNumber
        holder.phone.setOnClickListener {
            context!!.startActivity(
                Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse("tel:"+contact.phoneNumber))
            )
        }
    }

    override fun getItemCount(): Int {
        return contactList.size
    }
}