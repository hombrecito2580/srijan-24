package com.example.srijan24.ui

import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.srijan24.R
import com.example.srijan24.databinding.FragmentMerchandiseBinding
import com.example.srijan24.adapter.MerchandiseCarouselAdapter
import com.example.srijan24.view_model.MerchandiseViewModel
import com.example.srijan24.data.DetailsDataModel
import com.example.srijan24.retrofit.NetworkService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlin.math.abs

class MerchandiseFragment : Fragment() {
    companion object {
        fun newInstance() = MerchandiseFragment()
        const val REQUEST_CODE_IMAGE = 101
    }

    private var isSizeSelected = 0
    private var isImageUploaded = 0
    private var selectedImageUri: Uri? = null
    private lateinit var viewModel: MerchandiseViewModel
    private lateinit var dataModel: DetailsDataModel
    private lateinit var binding: FragmentMerchandiseBinding
    private lateinit var viewPager: ViewPager2
    private val networkService = NetworkService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMerchandiseBinding.inflate(inflater,container,false)
        val view = binding.root
        viewPager = binding.viewPagerCorousel

        val merchandise_images_data = arrayOf(
            R.drawable.ic_merchandise , R.drawable.ic_merchandise,
            R.drawable.ic_merchandise, R.drawable.ic_merchandise,
            R.drawable.ic_merchandise,R.drawable.ic_merchandise,R.drawable.ic_merchandise)
        viewPager.adapter = MerchandiseCarouselAdapter(merchandise_images_data)

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer((40 * Resources.getSystem().displayMetrics.density).toInt()))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = (0.80f + r * 0.20f)
        }
        viewPager.setPageTransformer(compositePageTransformer)

        addDotsIndicator(merchandise_images_data.size)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback()
        {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDots(position)
            }
        })

        binding.chooseSize.setOnClickListener {
            showSizeMenu(view)
            isSizeSelected = 1}
        binding.choosePaymentSs.setOnClickListener {
//            selectImage()
            isImageUploaded=1}

        binding.placeOrderButton.setOnClickListener {
//            placeOrder()
        }

        return view
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
    var selectedSizeIndex = 0;
    var selectedSize : String? = null
    fun showSizeMenu(view: View)
    {

        val t_shirt_size = arrayOf("XS","S","M","L","XL","2XL","3XL")
        selectedSize = t_shirt_size[selectedSizeIndex]
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Choose Size")
            .setSingleChoiceItems(t_shirt_size, selectedSizeIndex){ dialog, which ->
                selectedSizeIndex = which
                selectedSize = t_shirt_size[selectedSizeIndex]
            }
            .setPositiveButton("OK"){dialog,which ->
                showSnackBar("$selectedSize selected")
                binding.chooseSize.text = t_shirt_size[selectedSizeIndex]

                //implement here the size part
            }
            .setNeutralButton("Cancel"){dialog,which ->
                Toast.makeText(requireContext(),"Size is required", Toast.LENGTH_LONG).show()
            }
            .show()
    }
    private fun showSnackBar(msg : String)
    {
        Snackbar.make(binding.root,msg, Snackbar.LENGTH_SHORT).show()
    }


}