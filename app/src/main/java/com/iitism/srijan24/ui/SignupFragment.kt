package com.iitism.srijan24.ui

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(layoutInflater)
        viewmodel = ViewModelProvider(this)[SignupViewModel::class.java]
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
            }
        }
    }

    private fun createAccountAndRedirect() {
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

        viewmodel.uploadCredentials(request,
            { code ->
                when (code) {
                    201 -> {
                        findNavController().navigate(R.id.action_signupFragment_to_otpFragment)
                    }

                    500 -> {
                        Toast.makeText(
                            requireContext(),
                            "Couldn't register user... Please try again later",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    404 -> {
                        Toast.makeText(
                            requireContext(),
                            "Unexpected error occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    422 -> {
                        Toast.makeText(
                            requireContext(),
                            "Email already registered",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }, { isSuccessful ->
                if (!isSuccessful) {
                    Toast.makeText(requireContext(), "Internal server error...", Toast.LENGTH_SHORT)
                        .show()
                }
            })
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
            if (!email.endsWith("@iitism.ac.in")) {
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

}