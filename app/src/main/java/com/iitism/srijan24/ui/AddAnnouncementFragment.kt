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
import com.iitism.srijan24.R
import com.iitism.srijan24.data.CreateAnnouncementModel
import com.iitism.srijan24.data.PushNotification
import com.iitism.srijan24.databinding.FragmentAddAnnouncementBinding
import com.iitism.srijan24.retrofit.AnnouncementApi
import com.iitism.srijan24.retrofit.AnnouncementRetrofitInstance
import com.iitism.srijan24.retrofit.NotificationRetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddAnnouncementFragment : Fragment() {
    private val topic = "/topics/announcement"
    private var _binding: FragmentAddAnnouncementBinding? = null
    private val binding get() = _binding!!
    private lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddAnnouncementBinding.inflate(inflater, container, false)
        initializeDialog()
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
        dialog.show()
        val newAnnouncement = CreateAnnouncementModel(title, body)

        val announcementApi: AnnouncementApi = AnnouncementRetrofitInstance.announcementApi
        val createAnnouncementCall: Call<Void> = announcementApi.createAnnouncement(newAnnouncement)

        createAnnouncementCall.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
//                    sendNotifications(title, body)
                    binding.etTitle.setText("")
                    binding.etBody.setText("")
                    sendNotificationsToTopic(newAnnouncement)
                } else {
                    Log.e("CreateAnnouncement", "Failed to create announcement. Code: " + response.code())
                    Toast.makeText(context, "Failed to create announcement", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("CreateAnnouncement", "Network request failed", t)
            }
        })
    }

    private fun sendNotificationsToTopic(newAnnouncement: CreateAnnouncementModel) {
        PushNotification(newAnnouncement, topic)
            .also {
                sendNotification(it)
            }
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = NotificationRetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d("POST NOTIFICATION", "Notification sent")
                withContext(Dispatchers.Main) {
                    context?.let {
                        Toast.makeText(it, "Announcement made successfully!", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }
            }
        } catch (e: Exception) {
            Log.e("POST NOTIFICATION", e.toString())
            withContext(Dispatchers.Main) {
                context?.let {
                    Toast.makeText(it, "Failed to send announcement!", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
        }
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
            dialog.window!!.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(requireContext(), R.color.bg)))
        }
    }

//    private fun sendNotifications(title: String, body: String) {
//        val newAnnouncement = CreateAnnouncementModel(title, body)
//
//        val tokenApi: TokenApi = TokenRetrofitInstance.tokenApi
//        val getTokensCall: Call<GetTokensResponse> = tokenApi.getTokens()
//
//        getTokensCall.enqueue(object : Callback<GetTokensResponse> {
//            override fun onResponse(
//                call: Call<GetTokensResponse>,
//                response: Response<GetTokensResponse>,
//            ) {
//                if(response.isSuccessful) {
//                    val tokensResponse = response.body()
//                    if (tokensResponse != null && tokensResponse.success) {
//                        val tokens = tokensResponse.data
//                    } else {
//                        Log.e("GetTokens", "Unsuccessful response or empty data")
//                    }
//                } else {
//                    Log.d("GetTokens", "response not successful")
//                }
//            }
//
//            override fun onFailure(call: Call<GetTokensResponse>, t: Throwable) {
//                Log.e("GetTokens", "Network request failed", t)
//            }
//
//        })
//    }
}