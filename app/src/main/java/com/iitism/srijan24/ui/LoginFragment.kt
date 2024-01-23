package com.iitism.srijan24.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.iitism.srijan24.R
import com.iitism.srijan24.data.LoginDataModel
import com.iitism.srijan24.databinding.FragmentLoginBinding
import com.iitism.srijan24.view_model.LoginViewModel
import com.auth0.android.jwt.JWT


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginViewModel
    private lateinit var dialog: Dialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater)
        initializeDialog()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        binding.registerTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment2)
        }

        binding.loginBtn.setOnClickListener {
            if (validateEmail() && validatePassword())
                loginAndRedirect()
        }

    }

    private fun loginAndRedirect() {
        dialog.show()
        val request = LoginDataModel()
        binding.apply {
            request.apply {
                email = etEmail.text.toString()
                password = etPassword.text.toString()
            }
        }


        viewModel.checkCredentials(
            request
        ) { code ->
            Log.d("response code login", code.toString())
            when (code) {
                500 -> {
                    dialog.dismiss()
                    Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                }


                422 -> {
                    dialog.dismiss()
                    Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
                }

                403 -> {
                    dialog.dismiss()
                    Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                }

                200 -> {
                    dialog.dismiss()
                    val preferences = requireActivity().getSharedPreferences(
                        "MyPreferences",
                        Context.MODE_PRIVATE
                    )

                    preferences.edit().putString("token", viewModel.responseBody!!.token)
                        .apply()
                    preferences.edit().putString("email", binding.etEmail.text.toString().trim())
                        .apply()
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

                    Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    activity?.finish()
                }

                1000 -> {
                    dialog.dismiss()
                    Toast.makeText(context, "Internal server error", Toast.LENGTH_SHORT)
                        .show()
                }

                404 -> {
                    dialog.dismiss()
                    Toast.makeText(context, "Internet error, please try again", Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    dialog.dismiss()
                    Toast.makeText(context, "Unexpected error occurred", Toast.LENGTH_SHORT)
                        .show()
                }
            }
//
        }
    }

    private fun isISMite(): Boolean {

        return binding.etEmail.text.toString().trim().endsWith("@iitism.ac.in")

    }


    private fun validateEmail(): Boolean {
        var error: String? = null
        val email = binding.etEmail.text.toString().trim()
        if (email.isEmpty()) {
            error = "Please enter your email."
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            error = "Invalid email address."
        }

        if (error != null) {
            binding.etEmail.error = error
        } else {
            binding.etEmail.error = null
        }

        return error == null
    }

    private fun validatePassword(): Boolean {
        var error: String? = null
        val password = binding.etPassword.text.toString().trim()
        if (password.isEmpty()) {
            error = "Please enter your password."
        } else if (password.length < 6) {
            error = "Password is too short."
        }

        if (error != null) {
            binding.etPassword.error = error
        } else {
            binding.etPassword.error = null
        }

        return error == null
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