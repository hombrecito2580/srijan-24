package com.iitism.srijan24.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.iitism.srijan24.R
import com.iitism.srijan24.databinding.FragmentLoginBinding
import com.iitism.srijan24.databinding.FragmentMerchandiseBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val registerBtn = binding.registerTextView
        registerBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment2)
        }
    }


}