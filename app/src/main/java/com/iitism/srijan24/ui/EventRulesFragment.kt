package com.iitism.srijan24.ui

import EventDataModel
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.iitism.srijan24.R
import com.iitism.srijan24.adapter.RulesAdapter
import com.iitism.srijan24.databinding.FragmentAboutEventBinding
import com.iitism.srijan24.databinding.FragmentEventRulesBinding

class EventRulesFragment(val eventData:EventDataModel) : Fragment() {
    private var _binding: FragmentEventRulesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter:RulesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding=FragmentEventRulesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter= RulesAdapter(eventData.rules!!,requireContext())
        binding!!.rvRules.layoutManager = LinearLayoutManager(requireContext())
        binding!!.rvRules.adapter = adapter
        binding!!.rvRules.setHasFixedSize(true)

        binding.pdfLink.setOnClickListener {
            if (eventData.ruleBookLink != null) {
                val url = eventData.ruleBookLink
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                intent.setPackage("com.android.chrome")
                Log.i("pdflink",intent.data.toString())
                startActivity(intent)

            }
            else
                Toast.makeText(context, "No PDF Link available", Toast.LENGTH_SHORT).show()
        }
    }
}