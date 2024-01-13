package com.iitism.srijan24.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iitism.srijan24.data.ProfileDataModel
import com.iitism.srijan24.databinding.FragmentProfileBinding
import com.iitism.srijan24.view_model.ProfileViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences = context?.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val id= preferences?.getString("userId",null)
        val credential=ProfileDataModel()
        credential=profileViewModel.getCredentials(id!!,{

        },{

        })
        binding.tvUserName.text= preferences!!.getString("Username", null)
        binding.tvEmail.text= preferences!!.getString("Email", null)
        binding.tvContact.text= preferences!!.getString("Contact", null)
    }

}