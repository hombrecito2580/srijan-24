package com.iitism.srijan24.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
        setEditTextListeners(binding.editText1, binding.editText2, binding.editText3, binding.editText4, binding.editText5, binding.editText6)

        email = args.email

        binding.btnVerifyOtp.setOnClickListener {
//            val otp = binding.otpView.text.toString()
            val otp = getOtpFromEditTexts(binding.editText1, binding.editText2, binding.editText3, binding.editText4, binding.editText5, binding.editText6)
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
                            if (isISMite()) {
                                preferences.edit().putString("isISMite", "true").apply()

                            } else {
                                preferences.edit().putString("isISMite", "false").apply()

                            }
                            val jwt = JWT(viewModel.responseBody!!.token)
                            val userId: String? = jwt.getClaim("UserId").asString()

                            if (userId != null) {
                                preferences.edit().putString("userId", userId).apply()
                            }

//                            if (!isISMite())
//                            {
//                                findNavController().navigate(R.id.action_signupFragment_to_plansFragment)
//                            }
//                            else
//                            {
                                startActivity(Intent(requireContext(), MainActivity::class.java))
                                activity?.finish()
                            //}

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

    private fun setEditTextListeners(vararg editTexts: EditText) {
        for ((index, editText) in editTexts.withIndex()) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    // Do nothing
                }

                override fun onTextChanged(
                    charSequence: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    if (charSequence.length == 1 && index < editTexts.size - 1) {
                        // Move focus to the next EditText
                        editTexts[index + 1].requestFocus()
                    } else if (charSequence.isEmpty() && index > 0) {
                        // Move focus to the previous EditText
                        editTexts[index - 1].requestFocus()
                    } else if (charSequence.length == 1 && index == editTexts.size - 1) {
                        // Entered text in the last EditText, hide the keyboard
                        hideKeyboard()
                    }
                }

                override fun afterTextChanged(editable: Editable) {
                    // Do nothing
                }
            })
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }

    private fun isISMite(): Boolean {
        return email.endsWith("@iitism.ac.in")
    }

    private fun getOtpFromEditTexts(vararg editTexts: EditText): String {
        val otpStringBuilder = StringBuilder()
        for (editText in editTexts) {
            otpStringBuilder.append(editText.text)
        }
        return otpStringBuilder.toString()
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