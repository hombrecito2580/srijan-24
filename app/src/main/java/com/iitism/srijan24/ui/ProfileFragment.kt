package com.iitism.srijan24.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.iitism.srijan24.data.ProfileDataModel
import com.iitism.srijan24.databinding.FragmentProfileBinding
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.iitism.srijan24.R
import com.iitism.srijan24.data.GetUserResponse
import com.iitism.srijan24.retrofit.UserApiInstance
import retrofit2.Call
import retrofit2.Response

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var isISMite: String
    private lateinit var token: String

    private lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        initializeDialog()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences =
            requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        preferences?.getString("token",null)
        ProfileDataModel()
        binding.tvUserName.text= preferences.getString("Username", null)
        binding.tvEmail.text= preferences.getString("Email", null)
        binding.tvContact.text= preferences.getString("Contact", null)

        isISMite = preferences.getString("isISMite", "") ?: ""
        token = preferences.getString("token", "") ?: ""

        if(token.isEmpty()){
            findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
            startActivity(Intent(requireContext(),LoginSignupActivity::class.java))
        } else {
            Log.d("token", token)

            dialog.show()
            val call = UserApiInstance.createUserApi(token).getUser()

            call.enqueue(object : retrofit2.Callback<GetUserResponse> {
                override fun onResponse(
                    call: Call<GetUserResponse>,
                    response: Response<GetUserResponse>
                ) {
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        binding.txtProfile.text = body.name[0].toString()
                        binding.tvUserName.text = body.name
                        binding.tvEmail.text = body.email
                        binding.tvContact.text = body.phoneNumber
                    } else {
                        Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                    dialog.dismiss()
                }

                override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                    Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show()
                    Log.e("EEEEEEEEEEEEEEEE", t.toString())
                    findNavController().popBackStack()
                    dialog.dismiss()
                }
            })
        }

        binding.btnLogOut.setOnClickListener {
            preferences.edit().clear().apply()
            findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
            Toast.makeText(requireContext(), "Logged out successfully ", Toast.LENGTH_SHORT).show()
        }
 

    }

    override fun onResume() {
        super.onResume()

        if(token.isEmpty()){
            findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
            startActivity(Intent(requireContext(),LoginSignupActivity::class.java))
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