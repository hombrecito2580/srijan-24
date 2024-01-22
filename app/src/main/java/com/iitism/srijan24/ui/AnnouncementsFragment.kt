package com.iitism.srijan24.ui

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.iitism.srijan24.R
import com.iitism.srijan24.adapter.AnnouncementsRVAdapter
import com.iitism.srijan24.data.Announcement
import com.iitism.srijan24.databinding.FragmentAnnouncementsBinding
import com.iitism.srijan24.retrofit.AnnouncementRetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnnouncementsFragment : Fragment(), AnnouncementsRVAdapter.OnTimestampUpdateListener {
    private var _binding: FragmentAnnouncementsBinding? = null
    private val binding get() = _binding!!
    private lateinit var dialog: Dialog

    private lateinit var announcementsAdapter: AnnouncementsRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAnnouncementsBinding.inflate(inflater, container, false)
        initializeDialog()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        announcementsAdapter = AnnouncementsRVAdapter(requireContext(), this)
        binding.rvAnnouncements.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = announcementsAdapter
        }
        fetchAnnouncements()

        binding.btnRefresh.setOnClickListener {
            fetchAnnouncements()
        }
    }

    private fun fetchAnnouncements() {
        dialog.show()
        val call = AnnouncementRetrofitInstance.announcementApi.getAnnouncements()

        call.enqueue(object : Callback<List<Announcement>> {
            override fun onResponse(call: Call<List<Announcement>>, response: Response<List<Announcement>>) {
                if (response.isSuccessful) {
                    val announcements = response.body()
                    announcements?.let {
                        announcementsAdapter.refreshAnnouncements(it)
                        announcementsAdapter.updateTimestamps()
//                        Toast.makeText(context, "Announcements fetched successfully!", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                        Log.d("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq", "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq")
                    }
                } else {
                    Toast.makeText(context, "Failed to load data...", Toast.LENGTH_SHORT).show()
                    Log.d("dddddddddddddddddddddddddddd", response.code().toString())
                    dialog.dismiss()
                }
            }

            override fun onFailure(call: Call<List<Announcement>>, t: Throwable) {
                Log.e("FetchAnnouncements", "Network request failed", t)
                dialog.dismiss()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeDialog() {
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_bar)
        dialog.setCancelable(false)
//        dialog.setOnCancelListener {
////            Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show()
////            findNavController().popBackStack()
//        }
        val layoutParams = WindowManager.LayoutParams().apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.MATCH_PARENT
        }
        dialog.window?.attributes = layoutParams
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(requireContext(), R.color.bg)))
        }
    }

    override fun onUpdateTimestamp(position: Int, timeAgo: String) {
        // Update the timestamp in your ViewHolder or any other necessary logic
        binding.rvAnnouncements.findViewHolderForAdapterPosition(position)?.let { viewHolder ->
            (viewHolder as? AnnouncementsRVAdapter.AnnouncementsViewHolder)?.tvTime?.text = timeAgo
        }
    }
}
