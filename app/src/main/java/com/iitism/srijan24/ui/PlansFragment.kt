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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.iitism.srijan24.R
import com.iitism.srijan24.data.GetUserResponse
import com.iitism.srijan24.databinding.FragmentPlansBinding
import com.iitism.srijan24.retrofit.UserApiInstance
import retrofit2.Call
import retrofit2.Response

class PlansFragment : Fragment() {
    private var _binding: FragmentPlansBinding? = null
    private val binding get() = _binding!!
    private lateinit var dialog: Dialog

    private lateinit var userName: String
    private lateinit var contact: String
    private lateinit var email: String

    private var amount = 0
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlansBinding.inflate(inflater, container, false)
        initializeDialog()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preferences =
            requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        token = preferences.getString("token", "") ?: ""

        if (token.isEmpty()) {
            findNavController().navigate(R.id.action_plansFragmentDrawer_to_homeFragment)
            startActivity(Intent(requireContext(), LoginSignupActivity::class.java))
        } else {
            val call = UserApiInstance.createUserApi(token).getUser()

            call.enqueue(object : retrofit2.Callback<GetUserResponse> {
                override fun onResponse(
                    call: Call<GetUserResponse>,
                    response: Response<GetUserResponse>,
                ) {
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        userName = body.name
                        contact = body.phoneNumber
                        email = body.email
                        val sharedPreferences =
                            requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("userName", userName)
                        editor.putString("contact", "+91" + contact)
                        editor.putString("email", email)
                        editor.apply()

                    } else {
                        Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                }

                override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                    Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show()
                    Log.e("EEEEEEEEEEEEEEEE", t.toString())
                    findNavController().popBackStack()
                }
            })
        }

        val intent = Intent(requireContext(), PlansActivity::class.java)
        binding.btnPlatinum.setOnClickListener {
            amount = 1999
            intent.putExtra("amount", amount)
            startActivity(intent)
        }

        binding.btnGold.setOnClickListener {
            amount = 1799
            intent.putExtra("amount", amount)
            startActivity(intent)

        }

        binding.btnSilver.setOnClickListener {
            amount = 1499
            intent.putExtra("amount", amount)
            startActivity(intent)

        }

        binding.btnBronze.setOnClickListener {
            amount = 1199
            intent.putExtra("amount", amount)
            startActivity(intent)

        }
    }

    override fun onResume() {
        super.onResume()
        if (token.isEmpty()) {
            findNavController().navigate(R.id.action_merchandiseFragment_to_homeFragment)
            startActivity(Intent(requireContext(), LoginSignupActivity::class.java))
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