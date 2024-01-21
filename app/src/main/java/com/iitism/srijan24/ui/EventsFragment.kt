package com.iitism.srijan24.ui

import EventDataModel
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.iitism.srijan24.R
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
    ): View {
        // Inflate the layout for this fragment
        _binding=FragmentEventsBinding.inflate(inflater)
        initializeDialog()
        viewModel = ViewModelProvider(this)[EventsViewModel::class.java]
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog.show()

        viewModel.getEvents{
            if (it){
                adapter=EventsAdapter(viewModel.eventList.value!!,requireContext())
                binding!!.rvEvents.layoutManager = LinearLayoutManager(requireContext())
                binding!!.rvEvents.adapter = adapter
                dialog.dismiss()
//                binding.rvEvents.no
            } else {
                Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                findNavController().popBackStack()
            }
        }

        binding!!.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
//                val events= mutableListOf<EventDataModel>()
//                for (event in viewModel.eventList.value!!){
//                    if(event.eventName!!.lowercase().contains(newText.lowercase())){
//                        events.add(event)
//                    }
//                }
//                Toast.makeText(context, events.size.toString(), Toast.LENGTH_SHORT).show()
////                adapter=EventsAdapter(events,context)
//                adapter.notifyDataSetChanged()
                return true
            }
        })

    }


    private fun initializeDialog() {
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_bar)
        dialog.setCancelable(false)
        val layoutParams = WindowManager.LayoutParams().apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.MATCH_PARENT
        }
        dialog.window?.attributes = layoutParams
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.progress_bar
                    )
                )
            )
        }
    }

}