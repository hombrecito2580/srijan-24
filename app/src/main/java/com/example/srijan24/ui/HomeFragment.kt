package com.example.srijan24.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import com.example.srijan24.R
import com.example.srijan24.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
//    private lateinit var videoView: VideoView
//    private val path = "android.resource://com.example.srijan24/"+R.raw.bg_video
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val view = binding.root
        return  view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        videoView = binding.videoview
//        videoView.setVideoPath(path)
//        videoView.start()
//        videoView.setOnCompletionListener {
//            videoView.start()
//        }
    }
    override fun onResume() {

//        videoView.start()
        super.onResume()
    }

}