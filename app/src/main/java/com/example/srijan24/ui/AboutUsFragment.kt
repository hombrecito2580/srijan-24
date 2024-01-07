package com.example.srijan24.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.srijan24.R
import com.example.srijan24.databinding.FragmentAboutUsBinding

class AboutUsFragment : Fragment() {

    companion object {
        fun newInstance() = AboutUsFragment()
    }

    private lateinit var binding : FragmentAboutUsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutUsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvAboutUs.text="Established in 1926, the Indian Institute of Technology (Indian School of " +
                "Mines) Dhanbad, erstwhile Indian School of Mines Dhanbad, stands as a " +
                "prestigious engineering and research institution in the heart of the Coal " +
                "Capital of India, Dhanbad. Approaching its centenary, IIT (ISM) holds " +
                "the esteemed Institute of National Importance and has evolved from its " +
                "initial focus on Earth Sciences and Engineering to encompass 18 academic " +
                "departments across various technical disciplines. " +
                "Standing as the third oldest among IITs, IIT (ISM) has experienced " +
                "significant growth beyond its roots in Earth Sciences. Notably, it " +
                "achieved the 26th position in the QS World University Rankings for " +
                "Mining and Mineral Engineering in 2022. The institute admits " +
                "undergraduate students through the Joint Entrance Examination " +
                "(Advanced), making it a beacon of academic excellence and innovation " +
                "that attracts students nationwide. " +
                "Integral to the institute's vibrant culture is \"SRIJAN,\" a Socio-Cultural " +
                "Festival that has been captivating audiences in Eastern India since 1977. With over 30,000 annual attendees from 200 colleges nationwide, " +
                "SRIJAN serves as a dynamic platform for budding talents. The festival, " +
                "boasting a prize pool exceeding INR 7.5 lakhs, provides participants with " +
                "a unique opportunity to showcase their creativity, personality, and " +
                "talents, fostering interaction and skill enhancement on a national scale.\n"

        binding.tvConvenerName.text="Prof. Sanjoy Mandal"

        binding.tvConvenerMessage.text="Identification of a country’s civilization has always been through the " +
                "culture its people follow and promote. It generates a sense of unity via " +
                "common values, beliefs, and customs and standards. When knowledge " +
                "and education are promoted in a culturally sensitive setting, everyone " +
                "benefits—individuals as well as the society, nation, or world at large. We are proud of India's distinct culture, which represents the diversity of " +
                "the country. Its innate creativity and culture are synonymous with much " +
                "of its social, economic, and other activities. " +
                "In keeping with the aforementioned, the IIT (ISM) Dhanbad's three-day " +
                "Annual Socio-Cultural Srijan Festival, SRIJAN '24, aims to foster a sense " +
                "of solidarity and unity in diversity through a range of events honouring " +
                "the various customs and cultures across our nation. The ideas of the next " +
                "generation drive our nation's continuous growth. " +
                "In response, SRIJAN '24 offers a friendly setting for showcasing one's " +
                "abilities and creative concepts that could advance social causes. It " +
                "includes a variety of competitions where participants can polish their " +
                "skills in dance, singing, poetry writing, arts, and other areas in addition to " +
                "competing, all leading up to the much-anticipated gala star night. Warmest greetings from Team SRIJAN '24, who look forward to your " +
                "kind attendance at the socio-cultural extravaganza to mark this " +
                "significant milestone in IIT (ISM) Dhanbad's history.\n"

        binding.tvCoConvener1Name.text="Prof. Arijit Bansal"

        binding.tvCoConvener2Name.text="Prof. Suresh K Yatirajula"

        Glide.with(requireContext())
            .load("https://res.cloudinary.com/dfr1kvie3/image/upload/v1703957539/prof_convener_mojstc.jpg")
            .placeholder(R.drawable.iv_srijan_light)
            .centerCrop()
            .into(binding.ivConvener)

        Glide.with(requireContext())
            .load("https://res.cloudinary.com/dkdratnao/image/upload/v1704404941/Co-convenor_xoepnr.png")
            .placeholder(R.drawable.iv_srijan_light)
            .centerCrop()
            .into(binding.ivCoConvener1)

        Glide.with(requireContext())
            .load("https://res.cloudinary.com/dkdratnao/image/upload/v1704403824/Co-convenor2_qp0smn.png")
            .placeholder(R.drawable.iv_srijan_light)
            .centerCrop()
            .into(binding.ivCoConvener2)
    }
}