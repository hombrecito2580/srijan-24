package com.example.srijan24.adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.srijan24.R
import com.example.srijan24.data.CoreTeamDataModel

class CoreTeamAdapter(private val dataList:List<CoreTeamDataModel>) :
    RecyclerView.Adapter<CoreTeamAdapter.ViewHolder>(){

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.coreteam_card_view,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val currentData = dataList[position]
//        Log.i("data",currentData.toString())
        if(currentData.image != null)
        {
//            Picasso.get().load(currentData.imageUri).into(holder.image_c)

            Glide.with(holder.itemView)
                .load(currentData.image)
                .placeholder(R.drawable.iv_srijan_light)
                .centerCrop()
                .into(holder.image_c)
        }

        holder.name_c.text = currentData.name

        if(currentData?.team != null)
            holder.team_c.text = currentData.team
        else
            holder.team_c.text = "Team"

        if(currentData?.position != null)
            holder.position_c.text = currentData.position
        else
            holder.position_c.text = "Postion"


        if(currentData.linkedin_url != null)
        {
            Log.i("linkedInUrl",currentData.linkedin_url)
            holder.linkedIn_c.setOnClickListener()
            {
                val url = currentData.linkedin_url
                val i = Intent()
                i.setPackage("com.android.chrome")
                i.action = Intent.ACTION_VIEW
                i.data = Uri.parse(url)
                ContextCompat.startActivity(it.context, i, null)
            }
        }

        if(currentData.insta_url!= null)
        {
            holder.insta_c.setOnClickListener()
            {
                val url = currentData.insta_url
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                ContextCompat.startActivity(it.context, intent, null)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val image_c = view.findViewById<ImageView>(R.id.org_image_c)
        val name_c = view.findViewById<TextView>(R.id.org_name_c)
        val position_c = view.findViewById<TextView>(R.id.org_position_c)
        val team_c = view.findViewById<TextView>(R.id.org_team_c)
        val linkedIn_c = view.findViewById<ImageView>(R.id.org_linkedin_c)
        val insta_c = view.findViewById<ImageView>(R.id.org_insta_c)

    }
}