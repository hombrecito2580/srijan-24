package com.iitism.srijan24.ui

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.iitism.srijan24.R
import com.iitism.srijan24.data.SignUpDataModel
import com.iitism.srijan24.databinding.FragmentSignupBinding
import com.iitism.srijan24.view_model.SignupViewModel

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private lateinit var viewmodel: SignupViewModel
    private val binding get() = _binding!!
    private lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSignupBinding.inflate(layoutInflater)
        viewmodel = ViewModelProvider(this)[SignupViewModel::class.java]
        initializeDialog()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }

        binding.btnSignup.setOnClickListener {
            if (validateName() && validateEmail() && validateContact() && validatePassword() && validateConfirmPassword() && validatePasswordAndConfirmPassword()) {
                createAccountAndRedirect()
//                val direction = SignupFragmentDirections.actionSignupFragmentToOtpFragment(
//                    binding.etEmail.text.toString().trim()
//                )
//                findNavController().navigate(direction)
            }
        }
    }

    private fun createAccountAndRedirect() {
        dialog.show()
        val request = SignUpDataModel()
        binding.apply {
            request.apply {
                isISM = btnYes.isChecked
                name = etName.text.toString().trim()
                contact = etPhone.text.toString().trim()
                email = etEmail.text.toString().trim()
                password = etPassword.text.toString().trim()
            }
        }

        viewmodel.uploadCredentials(request) { code ->
            when (code) {
                201 -> {
                    dialog.dismiss()
                    val direction = SignupFragmentDirections.actionSignupFragmentToOtpFragment(
                        binding.etEmail.text.toString().trim()
                    )
                    findNavController().navigate(direction)
//                        findNavController().navigate(R.id.action_signupFragment_to_otpFragment)
                }

                500 -> {
                    dialog.dismiss()
                    Toast.makeText(
                        requireContext(),
                        "Couldn't register user... Please try again later",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                404 -> {
                    dialog.dismiss()
                    Toast.makeText(
                        requireContext(),
                        "Unexpected error occurred",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                422 -> {
                    dialog.dismiss()
                    Toast.makeText(
                        requireContext(),
                        "Email already registered",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                1000 -> {
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "Internal server error", Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    dialog.dismiss()
                    Toast.makeText(context, "Unexpected error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validateName(): Boolean {
        val name = binding.etName.text.toString().trim()
        var error: String? = null

        if (name.isEmpty()) {
            error = "Please enter the name"
        }

        if (error != null) {
            binding.etName.error = error
            return false
        }

        return true
    }

    private fun validateEmail(): Boolean {
        var error: String? = null
        val email = binding.etEmail.text.toString().trim()
        if (email.isEmpty()) {
            error = "Please enter your email."
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            error = "Invalid email address."
        } else if (binding.btnYes.isChecked) {
            if (!email.endsWith("@iitism.ac.in")) {
                error = "Invalid IIT ISM id."
            }
        } else if (binding.btnNo.isChecked) {
            if (email.endsWith("@iitism.ac.in")) {
                error = "Invalid email."
            }
        }

        if (error != null) {
            binding.etEmail.error = error
        } else {
            binding.etEmail.error = null
        }

        return error == null
    }

    private fun validateContact(): Boolean {
        var error: String? = null
        val phone = binding.etPhone.text.toString().trim()
        if (phone.isEmpty()) {
            error = "Please enter your phone number."
        } else if (phone.length != 10) {
            error = "Invalid phone number."
        }

        if (error != null) {
            binding.etPhone.error = error
        } else {
            binding.etPhone.error = null
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

    private fun validateConfirmPassword(): Boolean {
        var error: String? = null
        val password = binding.etConfirmPassword.text.toString().trim()
        if (password.isEmpty()) {
            error = "Please enter your password."
        } else if (password.length < 6) {
            error = "Password is too short."
        }

        if (error != null) {
            binding.etConfirmPassword.error = error
        } else {
            binding.etConfirmPassword.error = null
        }

        return error == null
    }

    private fun validatePasswordAndConfirmPassword(): Boolean {
        var error: String? = null
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()
        if (password != confirmPassword) {
            error = "Passwords do not match."
        }

        if (error != null) {
            binding.etConfirmPassword.error = error
        } else {
            binding.etConfirmPassword.error = null
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
                        R.color.bg
                    )
                )
            )
        }
    }

}