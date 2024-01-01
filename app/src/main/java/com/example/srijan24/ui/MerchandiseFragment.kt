package com.example.srijan24.ui

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.srijan24.R
import com.example.srijan24.adapter.MerchandiseCarouselAdapter
import com.example.srijan24.data.DetailsDataModel
import com.example.srijan24.databinding.FragmentMerchandiseBinding
import com.example.srijan24.databinding.FragmentSponsorsBinding
import com.example.srijan24.retrofit.ApiResponse
import com.example.srijan24.retrofit.NetworkService
import com.example.srijan24.view_model.MerchandiseViewModel
import com.example.srijan24.view_model.SponsorViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import kotlin.math.abs


class MerchandiseFragment : Fragment() {
    companion object {
        fun newInstance() = MerchandiseFragment()
        const val REQUEST_CODE_IMAGE = 101
    }

    private val perUnitTshirtPrice = 399
    private var isSizeSelected = 0
    private var isImageUploaded = 0
    private var selectedImageUri: Uri? = null
    private lateinit var dataModel: DetailsDataModel
    private var _binding: FragmentMerchandiseBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPager: ViewPager2

    private lateinit var merchViewModel: MerchandiseViewModel
    private lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMerchandiseBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        merchViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[MerchandiseViewModel::class.java]

        dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.progress_bar)
        dialog.setCancelable(false)
        val layoutParams = WindowManager.LayoutParams().apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.MATCH_PARENT
        }
        dialog.window?.attributes = layoutParams
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.bg
                    )
                )
            )
            dialog.window!!.setBackgroundDrawableResource(R.color.transparent)


        }

        viewPager = binding.viewPagerCorousel

        val merchandise_images_data = arrayOf(
            R.drawable.ic_merchandise, R.drawable.ic_merchandise,
            R.drawable.ic_merchandise, R.drawable.ic_merchandise,
            R.drawable.ic_merchandise, R.drawable.ic_merchandise, R.drawable.ic_merchandise
        )
        viewPager.adapter = MerchandiseCarouselAdapter(merchandise_images_data)

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer((40 * Resources.getSystem().displayMetrics.density).toInt()))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = (0.80f + r * 0.20f)
        }
        viewPager.setPageTransformer(compositePageTransformer)

        addDotsIndicator(merchandise_images_data.size)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDots(position)
            }
        })

        // Price Calculation
        binding.editQuantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                return
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                return

            }

            override fun afterTextChanged(p0: Editable?) {

                binding.apply {
                    val selectedQuantity = editQuantity.text.toString()
                    if (selectedQuantity.isNotEmpty()) {
                        val totalPriceToPay = (selectedQuantity.toInt() * perUnitTshirtPrice).toString()
                        val textToShow = "Total Price: Rs.$totalPriceToPay"
                        totalPrice.visibility = View.VISIBLE
                        totalPrice.text = textToShow
                    } else {
                        totalPrice.visibility = View.INVISIBLE
                        totalPrice.text = ""
                    }
                }
            }

        })


        binding.chooseSize.setOnClickListener {
            showSizeMenu(view)
            isSizeSelected = 1
        }
        binding.choosePaymentSs.setOnClickListener {
            selectImage()
            isImageUploaded = 1
        }

        binding.placeOrderButton.setOnClickListener {
            placeOrder()
        }

    }

    private fun addDotsIndicator(size: Int) {
        val dots = arrayOfNulls<ImageView>(size)
        for (i in 0 until size) {
            dots[i] = ImageView(requireContext())
            dots[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.indicator_inactive
                )
            )
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            binding.dotLayout.addView(dots[i], params)
        }
        dots[0]?.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.indicator_active
            )
        )
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
    var selectedSize: String? = null
    fun showSizeMenu(view: View) {

        val t_shirt_size = arrayOf("XS", "S", "M", "L", "XL", "2XL", "3XL")
        selectedSize = t_shirt_size[selectedSizeIndex]
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Choose Size")
            .setSingleChoiceItems(t_shirt_size, selectedSizeIndex) { dialog, which ->
                selectedSizeIndex = which
                selectedSize = t_shirt_size[selectedSizeIndex]
            }
            .setPositiveButton("OK") { dialog, which ->
                showSnackBar("$selectedSize selected")
                binding.chooseSize.text = t_shirt_size[selectedSizeIndex]

                //implement here the size part
            }
            .setNeutralButton("Cancel") { dialog, which ->
                Toast.makeText(requireContext(), "Size is required", Toast.LENGTH_LONG).show()
            }
            .show()
    }

    private fun showSnackBar(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> {
                    Log.i("Tag", "Image Selected")
                    // Check if data is not null and contains the image URI
                    if (data != null && data.data != null) {
                        val imageUri = data.data
                        selectedImageUri = imageUri
                        val imageBase64 =
                            MyFileHandler(requireContext()).handleFile(selectedImageUri!!)
                        val imageName =
                            MyFileHandler(requireContext()).getFileName(selectedImageUri!!)
                        Log.i("image", imageBase64.toString())
                        showSnackBar("$imageName is selected")
                        binding.choosePaymentSs.text = imageName
                    }
                }
            }
        }
    }

    class MyFileHandler(private val context: Context) {
        fun handleFile(imageUri: Uri): String? {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val imageBytes = inputStream?.readBytes()
            inputStream?.close()

            if (imageBytes != null) {
                val base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT)
                Log.i("Base64Image", base64Image)
                return base64Image
            }
            return ""
        }

        val resolver = context.contentResolver

        fun getFileName(uri: Uri): String {
            val returnCursor: Cursor = resolver.query(uri, null, null, null, null)!!
            val nameIndex: Int = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            val name: String = returnCursor.getString(nameIndex)
            returnCursor.close()
            return name
        }

        fun getFilePathFromContentUri(context: Context, contentUri: Uri): String? {
            var filePath: String? = null
            val projection = arrayOf(MediaStore.Images.Media.DATA)

            val cursor = context.contentResolver.query(contentUri, projection, null, null, null)

            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    filePath = it.getString(columnIndex)
                }
            }

            return filePath
        }


    }


    private fun placeOrder() {

        dataModel = DetailsDataModel(
            binding.editName.text.toString(),
            binding.editName.text.toString(),
            binding.editName.text.toString(),
            binding.editName.text.toString(),
            binding.editName.text.toString(),
            binding.editName.text.toString(),
            binding.editName.text.toString()
        )

        binding.apply {
            dataModel.apply {
                name = editName.text.toString()
                admissionNumber = editAdmno.text.toString()
                mobileNumber = editPhone.text.toString()
                tShirtSize = selectedSize.toString()
                hostel = editHostel.text.toString()
                roomNumber = editRoomNo.text.toString()
                quantity = editQuantity.text.toString()
            }
        }

        var flag = 1


        if (dataModel.name.isEmpty()) {
            binding.editName.error = "Name can't be empty"
            Log.d("name", binding.editName.text.toString())
            flag = 0
        }
        if (dataModel.admissionNumber.isEmpty()) {
            binding.editAdmno.error = "Admission no. can't be empty"
            Log.d("admission no", dataModel.admissionNumber)
            flag = 0
        }

        if (dataModel.hostel.isEmpty()) {

            binding.editHostel.error = "Hostel name can't be empty"
            Log.d("hostel", dataModel.hostel)
            flag = 0
        }
        if (dataModel.mobileNumber.isEmpty()) {
            binding.editPhone.error = "Phone no. can't be empty"
            Log.d("phone", dataModel.mobileNumber)
            flag = 0
        }
        if (dataModel.roomNumber.isEmpty()) {
            binding.editRoomNo.error = "Room no. can't be empty"
            Log.d("room no", dataModel.roomNumber)
            flag = 0
        }
        if (dataModel.quantity.isEmpty()) {
            binding.editQuantity.error = "Quantity not selected"
            Log.d("quantity", dataModel.roomNumber)
            flag = 0
        }
        if (isSizeSelected == 0) {
            flag = 0
            Toast.makeText(context, "Size not Selected!!", Toast.LENGTH_SHORT).show()
        }
        if (selectedImageUri == null) {
            flag = 0
            Toast.makeText(context, "Image not Uploaded!!", Toast.LENGTH_SHORT).show()
        }


        if (flag == 1 && isSizeSelected == 1) {

            //api hit

            merchViewModel.uploadData(dataModel, selectedImageUri!!, requireContext())

            merchViewModel.showLoading.observe(viewLifecycleOwner) { showLoading ->
                if (showLoading) {
                    dialog.show()
                } else {
                    dialog.dismiss()

                    binding.chooseSize.text = "Choose Size"
                    binding.choosePaymentSs.text = "Payement Screenshot"
                    selectedSizeIndex = 0
                    binding.editName.text.clear()
                    binding.editAdmno.text.clear()
                    binding.editHostel.text.clear()
                    binding.editRoomNo.text.clear()
                    binding.editPhone.text.clear()
                    binding.editQuantity.text.clear()
                    selectedImageUri = null
                }
            }


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}