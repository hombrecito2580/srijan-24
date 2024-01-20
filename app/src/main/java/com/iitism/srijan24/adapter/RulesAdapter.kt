package com.iitism.srijan24.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iitism.srijan24.R

class RulesAdapter(val rulesList:List<String>,val context: Context?): RecyclerView.Adapter<RulesAdapter.RulesViewHolder>() {

    inner class RulesViewHolder(v: View):RecyclerView.ViewHolder(v){
        var rules: TextView =v.findViewById(R.id.tvRules)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RulesAdapter.RulesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rules_card, parent, false)
        return RulesViewHolder(view)
    }

    override fun onBindViewHolder(holder: RulesAdapter.RulesViewHolder, position: Int) {
        val rule=rulesList[position]
        holder.rules.text=rule
    }

    override fun getItemCount(): Int {
        return rulesList.size
    }
}