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
import com.bumptech.glide.Glide
import com.iitism.srijan24.R
import com.iitism.srijan24.adapter.ContactAdapter
import com.iitism.srijan24.databinding.FragmentAboutEventBinding

class AboutEventFragment(private val eventData:EventDataModel) : Fragment() {

    private var _binding: FragmentAboutEventBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var adapter: ContactAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding=FragmentAboutEventBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tveventName.text=eventData.eventName
        binding.tvEventFee.text=eventData.fees
        binding.tvVenue.text=eventData.venue
        binding.tvEventDescription.text=eventData.miniDescription

        adapter= ContactAdapter(eventData.contact!!,requireContext())
        binding.rvContact.layoutManager = LinearLayoutManager(requireContext())
        binding.rvContact.adapter = adapter
        binding.rvContact.setHasFixedSize(true)

        Glide.with(requireContext())
            .load(eventData.poster)
            .placeholder(R.drawable.srijan_modified_logo)
            .into(binding.eventImage)

        Glide.with(requireContext())
            .load(eventData.poster)
            .placeholder(R.drawable.srijan_modified_logo)
            .into(binding.ivEvent)

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

        binding.btnRegister.setOnClickListener {
            binding.cvRegister.visibility = View.VISIBLE
            binding.btnRegister.visibility = View.GONE
        }
    }

}