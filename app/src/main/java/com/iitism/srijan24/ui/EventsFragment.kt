package com.iitism.srijan24.ui

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.iitism.srijan24.adapter.EventsAdapter
import com.iitism.srijan24.databinding.FragmentEventsBinding
import com.iitism.srijan24.view_model.EventsViewModel


class EventsFragment : Fragment() {

    private var _binding:FragmentEventsBinding?=null
    private val binding get() = _binding
    private lateinit var viewModel: EventsViewModel
    private lateinit var dialog: Dialog
    private lateinit var adapter: EventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentEventsBinding.inflate(inflater)
        viewModel = EventsViewModel(requireContext())
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getEvents()

        adapter=EventsAdapter(viewModel.eventList,requireContext())
        binding!!.rvEvents.layoutManager = LinearLayoutManager(requireContext())
        binding!!.rvEvents.adapter = adapter
        binding!!.rvEvents.setHasFixedSize(true)
        Toast.makeText(requireContext(),viewModel.eventList.size.toString(),Toast.LENGTH_SHORT).show()
    }

}