package com.iitism.srijan24.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
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
import com.iitism.srijan24.adapter.ProfileAccommodationAdapter
import com.iitism.srijan24.adapter.ProfileRVAdapter
import com.iitism.srijan24.adapter.ProfileRVEventsAdapter
import com.iitism.srijan24.data.GetUserAccommodationResponseItem
import com.iitism.srijan24.data.GetUserEventsResponseItem
import com.iitism.srijan24.data.GetUserResponse
import com.iitism.srijan24.data.ProfileDataModel
import com.iitism.srijan24.databinding.FragmentProfileBinding
import com.iitism.srijan24.retrofit.UserApiInstance
import retrofit2.Call
import retrofit2.Response

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var adapter: ProfileRVAdapter
    private val binding get() = _binding!!

    private lateinit var isISMite: String
    private lateinit var token: String

    private lateinit var dialog: Dialog

    private lateinit var call1: Call<GetUserResponse>
    private lateinit var call2: Call<ArrayList<GetUserEventsResponseItem>>
    private lateinit var call3: Call<ArrayList<GetUserAccommodationResponseItem>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        initializeDialog()

        binding.rvMerch.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPlans.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvEvents.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences =
            requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        preferences?.getString("token", null)
        ProfileDataModel()
        binding.tvUserName.text = preferences.getString("Username", null)
        binding.tvEmail.text = preferences.getString("Email", null)
        binding.tvContact.text = preferences.getString("Contact", null)

        isISMite = preferences.getString("isISMite", "") ?: ""
        token = preferences.getString("token", "") ?: ""

        Log.d("tokennnnnn", token)

        if (token.isEmpty()) {
            findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
            startActivity(Intent(requireContext(), LoginSignupActivity::class.java))
        } else {
            Log.d("token", token)

            dialog.show()

            var counter = 0

            call1 = UserApiInstance.createUserApi(token).getUser()
            call2 = UserApiInstance.createUserApi(token).getUserEvents()
            call3 = UserApiInstance.createUserApi(token).getUserAccommodation()

            call1.enqueue(object : retrofit2.Callback<GetUserResponse> {
                override fun onResponse(
                    call: Call<GetUserResponse>,
                    response: Response<GetUserResponse>,
                ) {
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        Log.d("response body", response.body()!!.toString())
                        binding.txtProfile.text = body.name[0].toString()
                        binding.tvUserName.text = body.name
                        binding.tvEmail.text = body.email
                        binding.tvContact.text = body.phoneNumber

                        if (body.merchandise.isEmpty()) {
                            binding.rvMerch.visibility = View.GONE
                            binding.tvNoOrders.visibility = View.VISIBLE
                        } else {
                            binding.rvMerch.visibility = View.VISIBLE
                            binding.tvNoOrders.visibility = View.GONE
                        }
                        binding.rvMerch.adapter = ProfileRVAdapter(body.merchandise)
                        counter++
                        checkCompletion(counter)
                    } else {
                        dialog.dismiss()
                        cancelAllCalls()
                        Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show()
//                        findNavController().popBackStack()
                    }
                }

                override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                    dialog.dismiss()
                    cancelAllCalls()
                    Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show()
//                    findNavController().popBackStack()
                }
            })


            call2.enqueue(object : retrofit2.Callback<ArrayList<GetUserEventsResponseItem>> {
                override fun onResponse(
                    call: Call<ArrayList<GetUserEventsResponseItem>>,
                    response: Response<ArrayList<GetUserEventsResponseItem>>,
                ) {
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        if (body.isEmpty()) {
                            binding.rvPlans.visibility = View.GONE
                            binding.tvNoPlans.visibility=View.VISIBLE
                        }else{
                            binding.rvPlans.visibility = View.VISIBLE
                            binding.tvNoPlans.visibility=View.GONE
                        }
                        binding.rvEvents.adapter=ProfileRVEventsAdapter(body)

                        counter++
                        checkCompletion(counter)

                    } else {
                        dialog.dismiss()
                        cancelAllCalls()
                        Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show()
//                        findNavController().popBackStack()
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<GetUserEventsResponseItem>>,
                    t: Throwable,
                ) {
                    dialog.dismiss()
                    cancelAllCalls()
                    Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show()
//                    findNavController().popBackStack()
                }
            })


            call3.enqueue(object : retrofit2.Callback<ArrayList<GetUserAccommodationResponseItem>> {
                override fun onResponse(
                    call: Call<ArrayList<GetUserAccommodationResponseItem>>,
                    response: Response<ArrayList<GetUserAccommodationResponseItem>>,
                ) {
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        Log.d("abcdefgh", body.toString())
                        if (body.isEmpty()) {
                            binding.rvPlans.visibility = View.GONE
                            binding.tvNoPlans.visibility = View.VISIBLE
                        } else {
                            binding.rvPlans.visibility = View.VISIBLE
                            binding.tvNoPlans.visibility = View.GONE
                        }
                        binding.rvPlans.adapter = ProfileAccommodationAdapter(body)
                        counter++
                        checkCompletion(counter)
                    } else {
                        dialog.dismiss()
                        cancelAllCalls()
                        Log.d("abcdefgh", "Ayein?")
                        Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show()
//                        findNavController().popBackStack()
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<GetUserAccommodationResponseItem>>,
                    t: Throwable,
                ) {
                    dialog.dismiss()
                    cancelAllCalls()
                    Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show()
//                    findNavController().popBackStack()
                }
            })

        }

        binding.btnLogOut2.setOnClickListener {
            preferences.edit().clear().apply()
            findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
            Toast.makeText(requireContext(), "Logged out successfully ", Toast.LENGTH_SHORT).show()
        }


    }

    private fun cancelAllCalls() {
        call1.cancel()
        call2.cancel()
        call3.cancel()
    }

    private fun checkCompletion(counter: Int) {
        if (counter == 3)
            dialog.dismiss()
    }

    override fun onResume() {
        super.onResume()

        val preferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        preferences?.getString("token", null)

        token = preferences.getString("token", "") ?: ""

        if (token.isEmpty()) {
            findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
            startActivity(Intent(requireContext(), LoginSignupActivity::class.java))
        }
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