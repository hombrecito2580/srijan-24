package com.iitism.srijan24.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.auth0.android.jwt.JWT
import com.iitism.srijan24.R
import com.iitism.srijan24.databinding.FragmentOtpBinding
import com.iitism.srijan24.view_model.OTPViewModel

class OtpFragment : Fragment() {
    private var _binding: FragmentOtpBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: OTPViewModel
    private lateinit var dialog: Dialog

    private val args: OtpFragmentArgs by navArgs()
    private var email = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOtpBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[OTPViewModel::class.java]
        initializeDialog()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        email = args.email

        binding.btnVerifyOtp.setOnClickListener {
            val otp = binding.otpView.text.toString()
            if (otp.length != 6) {
                Toast.makeText(context, "Invalid OTP", Toast.LENGTH_SHORT).show()
            } else {
                dialog.show()
                viewModel.verifyOTP(otp, email) { code ->
                    when (code) {
                        404 -> {
                            dialog.dismiss()
                            Toast.makeText(context, "Error... Please try again", Toast.LENGTH_SHORT)
                                .show()
                        }

                        400 -> {
                            dialog.dismiss()
                            Toast.makeText(
                                context,
                                "OTP expired... Please try again",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        500 -> {
                            dialog.dismiss()
                            Toast.makeText(context, "Failed to create account", Toast.LENGTH_SHORT)
                                .show()
                        }

                        200 -> {
                            dialog.dismiss()
                            Toast.makeText(
                                context,
                                "Account successfully created",
                                Toast.LENGTH_SHORT
                            ).show()
                            val preferences = requireActivity().getSharedPreferences(
                                "MyPreferences",
                                Context.MODE_PRIVATE
                            )
                            preferences.edit().putString("token", viewModel.responseBody!!.token)
                                .apply()
                            preferences.edit().putString("email", email).apply()

                            val jwt = JWT(viewModel.responseBody!!.token)
                            val userId: String? = jwt.getClaim("UserId").asString()

                            if (userId != null) {
                                preferences.edit().putString("userId", userId).apply()
                            }

                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            activity?.finish()
                        }

                        1000 -> {
                            dialog.dismiss()
                            Toast.makeText(context, "Internal server error", Toast.LENGTH_SHORT)
                                .show()
                        }

                        else -> {
                            dialog.dismiss()
                            Toast.makeText(context, "Unexpected error occurred", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
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
            dialog.window!!.setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.bg
                    )
                )
            )
        }
    }
}