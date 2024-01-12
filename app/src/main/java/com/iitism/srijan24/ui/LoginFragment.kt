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
import com.iitism.srijan24.data.LoginDataModel
import com.iitism.srijan24.databinding.FragmentLoginBinding
import com.iitism.srijan24.view_model.LoginViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var loginViewModel: LoginViewModel
    var flag = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        binding.registerTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment2)
        }

        binding.loginBtn.setOnClickListener {
            if (validateEmail() && validatePassword())
                loginAndRedirect()
        }

    }

    private fun loginAndRedirect() {
        val request = LoginDataModel()
        binding.apply {
            request.apply {
                email = etEmail.text.toString()
                password = etPassword.text.toString()
            }
        }


        loginViewModel.checkCredentials(request,
            { code ->
                when (code) {
                    500 -> {
                        Toast.makeText(
                            requireContext(),
                            "Login failed... Please try again later",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    422 -> {
                        Toast.makeText(
                            requireContext(),
                            "Account doesn't exist",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    201 -> {

                    }
                }
            }, { isSuccessful ->
                if (!isSuccessful) {
                    Toast.makeText(requireContext(), "Internal server error...", Toast.LENGTH_SHORT)
                        .show()
                }
            })
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

}