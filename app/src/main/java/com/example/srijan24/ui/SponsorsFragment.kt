package com.example.srijan24.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.srijan24.R
import com.example.srijan24.adapter.SponsorRVAdapter
import com.example.srijan24.data.SponsorData
import com.example.srijan24.databinding.FragmentSponsorsBinding
import com.example.srijan24.view_model.SponsorViewModel

class SponsorsFragment : Fragment() {
    private var _binding: FragmentSponsorsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SponsorViewModel
    private lateinit var adapter: SponsorRVAdapter
    private lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSponsorsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[SponsorViewModel::class.java]

        dialog = Dialog(requireActivity())
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
                        R.color.bg
                    )
                )
            )
        }

        binding.rvSponsors.layoutManager = LinearLayoutManager(context)
        binding.rvSponsors.setHasFixedSize(true)

        // Initialize the adapter with an empty list
        adapter = SponsorRVAdapter(emptyList()) { redirectURL ->
            openUrlInBrowser(redirectURL)
        }
        // Set the adapter to the RecyclerView
        binding.rvSponsors.adapter = adapter

        viewModel.showLoading.observe(viewLifecycleOwner) { showLoading ->
            if (showLoading) {
                dialog.show()
            } else {
                dialog.dismiss()
            }
        }

        viewModel.sponsorData.observe(viewLifecycleOwner) { data ->
            // Update the adapter data and notify the changes
            adapter.setData(data)
        }

        // Trigger the data fetching when the fragment is created
        viewModel.fetchSponsorData()

        return binding.root
    }

    private fun openUrlInBrowser(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (browserIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(browserIntent)
        } else {
            // Handle the case where no activity can handle the intent
            // For example, display a message to the user or log a warning
            Toast.makeText(context, url, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
