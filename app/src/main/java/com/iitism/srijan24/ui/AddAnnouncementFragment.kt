package com.iitism.srijan24.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.iitism.srijan24.data.CreateAnnouncementModel
import com.iitism.srijan24.databinding.FragmentAddAnnouncementBinding
import com.iitism.srijan24.retrofit.AnnouncementApi
import com.iitism.srijan24.retrofit.AnnouncementRetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddAnnouncementFragment : Fragment() {
    private var _binding: FragmentAddAnnouncementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddAnnouncementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmit.setOnClickListener {
            if(binding.etTitle.text.isEmpty()) {
                Toast.makeText(context, "Please enter the title", Toast.LENGTH_SHORT).show()
            }
            else if(binding.etBody.text.isEmpty()) {
                Toast.makeText(context, "Please enter the body", Toast.LENGTH_SHORT).show()
            }
            else {
                val title = binding.etTitle.text.toString()
                val body = binding.etBody.text.toString()

                sendAnnouncement(title, body)
            }
        }
    }

    private fun sendAnnouncement(title: String, body: String) {
        val newAnnouncement = CreateAnnouncementModel(title, body)

        val announcementApi: AnnouncementApi = AnnouncementRetrofitInstance.announcementApi
        val createAnnouncementCall: Call<Void> = announcementApi.createAnnouncement(newAnnouncement)

        createAnnouncementCall.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Announcement created successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("CreateAnnouncement", "Failed to create announcement. Code: " + response.code())
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("CreateAnnouncement", "Network request failed", t)
            }
        })
    }
}