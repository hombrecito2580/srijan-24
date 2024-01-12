package com.iitism.srijan24.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.iitism.srijan24.R
import com.iitism.srijan24.adapter.HomeCarouselAdapter
import com.iitism.srijan24.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Timer
import java.util.TimerTask
import kotlin.math.abs


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val autoScrollDelay: Long = 3000
    private val totalPages = 6
    private var currentPage = 0
    private var timer: Timer? = null
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        countDownSrijanStart()

        val images = arrayOf(R.drawable.gallery_img_1 , R.drawable.gallery_img_2, R.drawable.gallery_img_3, R.drawable.gallery_img_4, R.drawable.gallery_img_5, R.drawable.gallery_img_6)
        binding.viewPagerCarousel.adapter = HomeCarouselAdapter(images)

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer((40 * Resources.getSystem().displayMetrics.density).toInt()))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = (0.80f + r * 0.20f)
        }
        binding.viewPagerCarousel.setPageTransformer(compositePageTransformer)

        addDotsIndicator(images.size)
        binding.viewPagerCarousel.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback()
        {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDots(position)
            }
        })

        startAutoScroll()

        binding.viewPagerCarousel.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentPage = position
            }
        })
    }

    override fun onResume() {
        super.onResume()
        restartAutoScroll()
    }

    override fun onPause() {
        super.onPause()
        stopAutoScroll()
    }

    private fun countDownSrijanStart(){
        val handler = android.os.Handler()
        val runnable = object : java.lang.Runnable {
            @SuppressLint("SetTextI18n")
            override fun run() {
                handler.postDelayed(this, 1000)
                try {
                    val currentDate = Date()
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val futureDate: Date = dateFormat.parse("2024-02-02 00:00:00")
                    if (!currentDate.after(futureDate)) {
                        var diff: Long = (futureDate.time
                                - currentDate.time)
                        val days = diff / (24 * 60 * 60 * 1000)
                        diff -= days * (24 * 60 * 60 * 1000)
                        val hours = diff / (60 * 60 * 1000)
                        diff -= hours * (60 * 60 * 1000)
                        val minutes = diff / (60 * 1000)
                        diff -= minutes * (60 * 1000)
                        val seconds = diff / 1000
                        binding.txtDay.text = "" + String.format("%02d", days)
                        binding.txtHour.text = "" + String.format("%02d", hours)
                        binding.txtMinute.text = "" + String.format("%02d", minutes)
                        binding.txtSecond.text = "" + String.format("%02d",seconds)
                    }
                    else {
                        countDownSrijanEnd()
                        binding.textcounterdown.text = "Srijan'23 is Live"
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        handler.postDelayed(runnable, 1 * 1000)

    }



    fun countDownSrijanEnd(){
        val handler = android.os.Handler()
        val runnable = object : java.lang.Runnable {
            @SuppressLint("SetTextI18n")
            override fun run() {
                handler.postDelayed(this, 1000)
                try {
                    val currentDate = Date()
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val futureDate: Date = dateFormat.parse("2024-02-05 00:00:00")
                    if (!currentDate.after(futureDate)) {

                        var diff: Long = (futureDate.time
                                - currentDate.time)
                        val days = diff / (24 * 60 * 60 * 1000)
                        diff -= days * (24 * 60 * 60 * 1000)
                        val hours = diff / (60 * 60 * 1000)
                        diff -= hours * (60 * 60 * 1000)
                        val minutes = diff / (60 * 1000)
                        diff -= minutes * (60 * 1000)
                        val seconds = diff / 1000
                        binding.txtDay.text = "" + String.format("%02d", days)
                        binding.txtHour.text = "" + String.format("%02d", hours)
                        binding.txtMinute.text = "" + String.format("%02d", minutes)
                        binding.txtSecond.text = "" + String.format("%02d",seconds)
                    }
                    else {
                        binding.textcounterdown.text = "Srijan'23 is  Over"
                        binding.txtDay.visibility = View.INVISIBLE
                        binding.txtHour.visibility = View.INVISIBLE
                        binding.txtMinute.visibility = View.INVISIBLE
                        binding.txtSecond.visibility = View.INVISIBLE

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        handler.postDelayed(runnable,  1 * 1000)
    }

    private fun addDotsIndicator(size: Int) {
        val dots = arrayOfNulls<ImageView>(size)
        for (i in 0 until size) {
            dots[i] = ImageView(requireContext())
            dots[i]?.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.indicator_inactive))
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            binding.dotLayout.addView(dots[i], params)
        }
        dots[0]?.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.indicator_active))
    }

    private fun updateDots(position: Int) {
        val childCount = binding.dotLayout.childCount
        for (i in 0 until childCount) {
            val dot = binding.dotLayout.getChildAt(i) as ImageView
            val drawableId =
                if (i == position) R.drawable.indicator_active else R.drawable.indicator_inactive
            dot.setImageDrawable(ContextCompat.getDrawable(requireContext(), drawableId))
        }
    }

    private fun startAutoScroll() {
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post {
                    if (currentPage == totalPages - 1) {
                        currentPage = 0
                    } else {
                        currentPage++
                    }
                    binding.viewPagerCarousel.setCurrentItem(currentPage, true)
                }
            }
        }, 0, autoScrollDelay)
    }

    private fun stopAutoScroll() {
        timer?.cancel()
        timer?.purge()
        timer = null
    }

    private fun restartAutoScroll() {
        stopAutoScroll()
        handler.postDelayed({ startAutoScroll() }, autoScrollDelay)
    }
}