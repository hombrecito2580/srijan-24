package com.iitism.srijan24.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iitism.srijan24.R
import com.iitism.srijan24.adapter.CoreTeamAdapter
import com.iitism.srijan24.view_model.CoreTeamViewModel

class CoreTeamFragment : Fragment() {

    private lateinit var viewModel: CoreTeamViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_core_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val CoreTeamRecyclerView : RecyclerView = view.findViewById(R.id.recycler_view_c)

        CoreTeamRecyclerView.layoutManager = LinearLayoutManager(context)
        CoreTeamRecyclerView.setHasFixedSize(true)

        viewModel = CoreTeamViewModel(requireContext())
        viewModel.getCoreTeamList()

        val itemAdapter = CoreTeamAdapter(viewModel.coreTeamList)
        CoreTeamRecyclerView.adapter = itemAdapter

    }

}