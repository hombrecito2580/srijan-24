package com.iitism.srijan24.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iitism.srijan24.R
import com.iitism.srijan24.databinding.FragmentPlansBinding

class PlansFragment : Fragment() {

    private var _binding: FragmentPlansBinding?=null
    private val binding get() = _binding
    var amount=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentPlansBinding.inflate(inflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.btnPlatinum.setOnClickListener{
            amount=1999
        }
        binding!!.btnGold.setOnClickListener{
            amount=1799
        }
        binding!!.btnSilver.setOnClickListener{
            amount=1499
        }
        binding!!.btnBronze.setOnClickListener{
            amount=1199
        }
    }

}