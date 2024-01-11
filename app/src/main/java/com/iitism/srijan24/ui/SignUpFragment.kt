package com.iitism.srijan24.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.iitism.srijan24.R
import com.iitism.srijan24.data.AuthDataModel
import com.iitism.srijan24.databinding.FragmentLoginBinding
import com.iitism.srijan24.databinding.FragmentSignUpBinding
import com.iitism.srijan24.view_model.AuthenticationViewModel
import com.iitism.srijan24.view_model.LoginViewModel
import com.iitism.srijan24.view_model.MerchandiseViewModel
import com.iitism.srijan24.view_model.SignUpViewModel

class SignUpFragment : Fragment() {

    init {
        fun newInstance() =LoginFragment()
    }

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var signUpViewModel: AuthenticationViewModel
    private lateinit var datamodel: AuthDataModel
    var flag=1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentSignUpBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUpViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[AuthenticationViewModel::class.java]

        binding.signupbtn.setOnClickListener{
            signup()
        }

    }

    private fun signup() {
        binding.apply {
            datamodel.apply {
                email=.text.toString()
                password=.text.toString()
            }
        }

        val pattern = Regex("""^\S+@\S+\.\S+$""")
        if (datamodel.email==null){
            binding.editEmail.helperText="Email should not be null"
            flag=0
        }
        else if(!pattern.matches(binding.editEmail.text)) {
            binding.editEmail.helperText = "Email is not in correct format"
            flag=0
        }

        if(datamodel.password.length < 6) {
            binding.editPassword.helperText = "Password must contain at least 6 characters"
            flag=0
        }

        if(flag==1){
            signUpViewModel.checkCredentials(datamodel,requireContext())
        }
    }

}