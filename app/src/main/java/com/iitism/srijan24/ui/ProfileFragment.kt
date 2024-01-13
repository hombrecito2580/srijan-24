package com.iitism.srijan24.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iitism.srijan24.data.ProfileDataModel
import com.iitism.srijan24.databinding.FragmentProfileBinding
import com.iitism.srijan24.view_model.ProfileViewModel
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.iitism.srijan24.R

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel

    private lateinit var isISMite: String
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences =
            requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val id= preferences?.getString("userId",null)
        val credential=ProfileDataModel()
//        credential=profileViewModel.getCredentials(id!!,{
//
//        },{
//
//        })
        binding.tvUserName.text= preferences!!.getString("Username", null)
        binding.tvEmail.text= preferences!!.getString("Email", null)
        binding.tvContact.text= preferences!!.getString("Contact", null)

        isISMite = preferences.getString("isISMite", "") ?: ""
        userId = preferences.getString("userId", "") ?: ""

        if(userId.isEmpty()){
            findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
            startActivity(Intent(requireContext(),LoginSignupActivity::class.java))
        }

        binding.btnLogOut.setOnClickListener {
            preferences.edit().clear().apply()
            findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
            Toast.makeText(requireContext(), "Logged out successfully ", Toast.LENGTH_SHORT).show()
        }
 

    }

    override fun onResume() {
        super.onResume()

        if(userId.isEmpty()){
            findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
            startActivity(Intent(requireContext(),LoginSignupActivity::class.java))
        }
    }

}