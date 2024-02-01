package com.iitism.srijan24.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.Cursor
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
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.cloudinary.Cloudinary
import com.cloudinary.android.MediaManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.iitism.srijan24.BuildConfig.CLOUDINARY_URL
import com.iitism.srijan24.BuildConfig.RAZORPAY_KEY
import com.iitism.srijan24.R
import com.iitism.srijan24.adapter.MerchandiseCarouselAdapter
import com.iitism.srijan24.data.DetailsDataModel
import com.iitism.srijan24.data.GetUserResponse
import com.iitism.srijan24.data.MakeOrderBody
import com.iitism.srijan24.data.MakeOrderResponse
import com.iitism.srijan24.databinding.FragmentMerchandiseBinding
import com.iitism.srijan24.retrofit.RazorpayRetrofitInstance
import com.iitism.srijan24.retrofit.UserApiInstance
import com.iitism.srijan24.view_model.MerchandiseViewModel
import com.razorpay.Checkout
import org.json.JSONObject
import com.razorpay.PaymentResultListener
import retrofit2.Call
import retrofit2.Response
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.math.abs
import kotlin.text.Charsets.UTF_8


class MerchandiseFragment : Fragment(), PaymentResultListener {
    //    private var merchandiseListener: MerchandiseListener? = null
    companion object {
        const val REQUEST_CODE_IMAGE = 101
    }

    private val cloudinary = Cloudinary(CLOUDINARY_URL)

//    private lateinit var config: HashMap<String, String>

    private var perUnitTShirtPrice = 399
    private var perUnitHoodiePrice = 799
    private var isSizeSelected = 0
    private var isMerchSelected = 0
    private var isImageUploaded = 0
    private var selectedImageUri: Uri? = null
    private lateinit var dataModel: DetailsDataModel
    private var _binding: FragmentMerchandiseBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPager: ViewPager2

    private lateinit var merchViewModel: MerchandiseViewModel
    private lateinit var dialog: Dialog
    private lateinit var isISMite: String
    private lateinit var token: String
    private var totalPriceToPay: Int = 0
    private lateinit var orderId: String

    //    val merchSize = arrayOf("XS", "S", "M", "L", "XL", "2XL", "3XL")
    private var merchSize = arrayOf<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        try {
            // Attempt to initialize MediaManager
            MediaManager.init(requireContext())
        } catch (e: IllegalStateException) {
            // MediaManager is already initialized, handle accordingly
        }
        _binding = FragmentMerchandiseBinding.inflate(inflater, container, false)

//        config = hashMapOf()

        merchViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[MerchandiseViewModel::class.java]

//        config["cloud_name"] = "digvpmszg"
//        config["api_key"] = "346224682169534"
//        config["api_secret"] = "c7Eip5uGeMBUYxU8ta4iGn51qPo"


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDialog()

        dialog.show()
        val preferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        isISMite = preferences.getString("isISMite", "") ?: ""
        token = preferences.getString("token", "") ?: ""

        if (token.isEmpty()) {
            findNavController().navigate(R.id.action_merchandiseFragment_to_homeFragment)
            startActivity(Intent(requireContext(), LoginSignupActivity::class.java))
            dialog.dismiss()
        } else {
//            if (isISMite == "true") perUnitTShirtPrice = 349
            val call = UserApiInstance.createUserApi(token).getUser()

            call.enqueue(object : retrofit2.Callback<GetUserResponse> {
                override fun onResponse(
                    call: Call<GetUserResponse>,
                    response: Response<GetUserResponse>,
                ) {
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        binding.editName.setText(body.name)
                        binding.editEmail.setText(body.email)
                        binding.editPhone.setText(body.phoneNumber)
                    } else {
                        Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                    dialog.dismiss()
                }

                override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                    Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show()
                    Log.e("EEEEEEEEEEEEEEEE", t.toString())
                    findNavController().popBackStack()
                    dialog.dismiss()
                }
            })
        }

        viewPager = binding.viewPagerCorousel

        val merchandiseImagesData = arrayOf(
            "https://res.cloudinary.com/dxomldckp/image/upload/v1705487638/srijan%2024/fae1pzqqziwqvzddpo9p.jpg",
            "https://res.cloudinary.com/dxomldckp/image/upload/v1705487629/srijan%2024/bjb66hakdbeymti2gk3g.jpg",
            "https://res.cloudinary.com/dxomldckp/image/upload/v1705310035/srijan%2024/nyko1lzetgqbxuzo9lwi.jpg",
            "https://res.cloudinary.com/dxomldckp/image/upload/v1705310023/srijan%2024/w1scw9cxlifqpmdaexkd.jpg",
            "https://res.cloudinary.com/dxomldckp/image/upload/v1706260919/srijan%2024/jc3cj53zhgb4wgs1mv64.png",
            "https://res.cloudinary.com/dxomldckp/image/upload/v1706260802/srijan%2024/uhb4ccj0atv6qc627fw4.png"
        )
        viewPager.adapter = MerchandiseCarouselAdapter(merchandiseImagesData)

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer((40 * Resources.getSystem().displayMetrics.density).toInt()))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = (0.80f + r * 0.20f)
        }
        viewPager.setPageTransformer(compositePageTransformer)

        addDotsIndicator(merchandiseImagesData.size)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDots(position)
            }
        })

//        binding.btnOrder.setOnClickListener {
//            val url = "https://forms.gle/VUK1fWcdLyXqNyxa9"
//            val intent = Intent(Intent.ACTION_VIEW)
//            intent.data = Uri.parse(url)
//            intent.setPackage("com.android.chrome")
//            startActivity(intent)
//        }
        // Price Calculation
        binding.editQuantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                return
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                return

            }

            override fun afterTextChanged(p0: Editable?) {
                if (isMerchSelected == 1 && isSizeSelected == 1) {
                    showPrice()

                }
            }

        })

        binding.chooseMerch.setOnClickListener {
            showMerchMenu(view)
        }
        binding.chooseSize.setOnClickListener {
            if (isMerchSelected == 1) showSizeMenu(view)
            else Toast.makeText(context, "Select Merchandise", Toast.LENGTH_SHORT).show()
        }
//        binding.choosePaymentSs.setOnClickListener {
//            selectImage()
//        }

//        binding.placeOrderButton.setOnClickListener {
//            placeOrder()
//        }

//        val config= HashMap<Any?, Any?>()
//        config["cloud_name"] = "digvpmszg"
//        config["secure"] = true
//        config["api_key"] = "346224682169534"
//        MediaManager.init(requireContext(), config)


        binding.payButton.setOnClickListener {
            startPayment(totalPriceToPay)
//            totalPriceToPay = 1
//            if (ContextCompat.checkSelfPermission(
//                    requireActivity(),
//                    Manifest.permission.READ_SMS
//                ) !=
//                PackageManager.PERMISSION_GRANTED
//            ) {
//                requestPermissions(
//                    arrayOf(Manifest.permission.READ_SMS),
//                    1001
//                )
//            } else {
//                startPayment(totalPriceToPay)
//            }
        }
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is MerchandiseListener) {
//            merchandiseListener = context
//        } else {
//            throw ClassCastException("$context must implement Merchandise")
//        }
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        merchandiseListener = null
//    }

    private fun initializeDialog() {
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_bar)
        dialog.setCancelable(false)
//        dialog.setOnCancelListener {
////            Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show()
//            findNavController().popBackStack()
//        }

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
                        R.color.progress_bar
                    )
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (token.isEmpty()) {
            findNavController().navigate(R.id.action_merchandiseFragment_to_homeFragment)
            startActivity(Intent(requireContext(), LoginSignupActivity::class.java))
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

    private var selectedSizeIndex = 0
    private var selectedSize: String? = null
    private fun showSizeMenu(view: View) {


        val customView = layoutInflater.inflate(R.layout.layout_custom_material_dialog, null)
        val materialDialog = MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setView(customView)
            .show()

        // Find the RadioGroup in the layout
        val radioGroup = materialDialog.findViewById<RadioGroup>(R.id.customDialogRadioGroup)!!

// Add radio buttons dynamically based on the array of options
        selectedSize = merchSize[selectedSizeIndex]
        for (i in merchSize.indices) {

            val radioButton = layoutInflater.inflate(
                R.layout.layout_custom_material_dialog_radio_button,
                null
            ) as RadioButton
            radioButton.text = merchSize[i]
            radioButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            // Add other properties and setOnClickListener if needed
            radioGroup.addView(radioButton)

            // Set the default selected radio button
            if (i == selectedSizeIndex) {
                radioButton.isChecked = true
            }

            radioButton.tag = i

            radioButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedSizeIndex = radioButton.tag as Int
                    selectedSize = merchSize[selectedSizeIndex]
                }
            }
        }

// Find and set onClickListeners for buttons
        val positiveButton = materialDialog.findViewById<Button>(R.id.customDialogPositiveBtn)
        positiveButton?.apply {
            text = "OK" // Set the button text if needed
            setOnClickListener {
                showSnackBar("$selectedSize selected")
                binding.chooseSize.text = merchSize[selectedSizeIndex]
                isSizeSelected = 1
                materialDialog.dismiss()
                showPrice()
            }
        }

        val neutralButton = materialDialog.findViewById<Button>(R.id.customDialogNeutralBtn)
        neutralButton?.apply {
            text = "CANCEL" // Set the button text if needed
            setOnClickListener {
                materialDialog.dismiss()
            }
        }
    }

    fun String.unescapeUnicode(): String {
        return Regex("""\\u([0-9a-fA-F]{4})""")
            .replace(this) {
                it.groupValues[1].toInt(16).toChar().toString()
            }
    }

    private fun showPrice() {
        binding.apply {
            if (isMerchSelected == 1 && isSizeSelected == 1) {

                val selectedQuantity = editQuantity.text.toString().trim()

                if (selectedQuantity.isNotEmpty()) {
                    if (chooseMerch.text == "T-Shirt") {
                        totalPriceToPay =
                            (selectedQuantity.toInt() * perUnitTShirtPrice)
                    } else if (chooseMerch.text == "Hoodie") {
                        totalPriceToPay =
                            (selectedQuantity.toInt() * perUnitHoodiePrice)
                    }

                    val textToShow = "Payable Amount: \\u20B9 $totalPriceToPay".unescapeUnicode()
                    totalPrice.text = textToShow
                }
            } else {

                totalPrice.text = "Payable Amount: \\u20B9 0".unescapeUnicode()
            }
        }
    }

    private var selectedMerchIndex = 0
    private var selectedMerch: String? = null
    private fun showMerchMenu(view: View) {

        val customViewMerch = layoutInflater.inflate(R.layout.layout_custom_material_dialog, null)
        val materialDialogMerch =
            MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
                .setView(customViewMerch)
                .show()

        // Find the RadioGroup in the layout
        val radioGroup = materialDialogMerch.findViewById<RadioGroup>(R.id.customDialogRadioGroup)!!

        // Add radio buttons dynamically based on the array of options
        val merchandiseArray = arrayOf("T-Shirt", "Hoodie")
        selectedMerch = merchandiseArray[selectedMerchIndex]
        for (i in merchandiseArray.indices) {
            val radioButton = layoutInflater.inflate(
                R.layout.layout_custom_material_dialog_radio_button,
                null
            ) as RadioButton
            radioButton.text = merchandiseArray[i]
            radioButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            // Add other properties and setOnClickListener if needed
            radioGroup.addView(radioButton)

            // Set the default selected radio button
            if (i == selectedMerchIndex) {
                radioButton.isChecked = true
            }

            radioButton.tag = i

            radioButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedMerchIndex = radioButton.tag as Int
                    selectedMerch = merchandiseArray[selectedMerchIndex]
                }
            }
        }

        materialDialogMerch.findViewById<TextView>(R.id.customDialogTitle)?.text = "Available Merchandise"

        // Find and set onClickListeners for buttons
        val positiveButton = materialDialogMerch.findViewById<Button>(R.id.customDialogPositiveBtn)
        positiveButton?.apply {
            text = "OK" // Set the button text if needed
            setOnClickListener {
                showSnackBar("$selectedMerch selected")
                binding.chooseMerch.text = merchandiseArray[selectedMerchIndex]
                isMerchSelected = 1
                if (selectedMerch == "T-Shirt") {
                    merchSize = arrayOf("XS", "S", "M", "L", "XL", "2XL")
                } else if (selectedMerch == "Hoodie") {
                    merchSize = arrayOf("XS", "S", "M", "L", "XL", "2XL", "3XL")

                }
                isSizeSelected = 0
                binding.chooseSize.text = "Choose Size"
                showPrice()
                materialDialogMerch.dismiss()
            }
        }

        val neutralButton = materialDialogMerch.findViewById<Button>(R.id.customDialogNeutralBtn)
        neutralButton?.apply {
            text = "CANCEL" // Set the button text if needed
            setOnClickListener {
                materialDialogMerch.dismiss()
            }
        }

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
                        isImageUploaded = 1
                        val imageBase64 =
                            MyFileHandler(requireContext()).handleFile(selectedImageUri!!)
                        val imageName =
                            MyFileHandler(requireContext()).getFileName(selectedImageUri!!)
                        Log.i("image", imageBase64.toString())
                        showSnackBar("$imageName is selected")
//                        binding.choosePaymentSs.text = imageName
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

        private val resolver = context.contentResolver

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

//    private fun placeOrder() {
//        dataModel = DetailsDataModel()
//
//        binding.apply {
//            dataModel.apply {
//                address = editAddress.text.toString().trim()
//                tShirtSize = selectedSize.toString().trim()
//                quantity = editQuantity.text.toString().trim()
//            }
//        }
//
//        var flag = 1
//
//        if (dataModel.address.trim().isEmpty()) {
//            binding.editAddress.error = "Address can't be empty"
//            Log.d("hostel", dataModel.address)
//            flag = 0
//        } else if (dataModel.quantity.trim().isEmpty() || dataModel.quantity.toInt() < 1) {
//            binding.editQuantity.error = "Quantity  can't be empty"
//            flag = 0
//        } else if (isSizeSelected == 0) {
//            flag = 0
//            Toast.makeText(context, "Size not Selected!!", Toast.LENGTH_SHORT).show()
//        } else if (selectedImageUri == null) {
//            flag = 0
//            Toast.makeText(context, "Image not Uploaded!!", Toast.LENGTH_SHORT).show()
//        }
//
//
//        if (flag == 1 && isSizeSelected == 1) {
//            dialog.show()
//
//            MediaManager.get().upload(selectedImageUri).unsigned("laqyxjqt").callback(
//                object : UploadCallback {
//                    override fun onStart(requestId: String?) {
//
//                    }
//
//                    override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
//
//                    }
//
//                    override fun onSuccess(
//                        requestId: String?,
//                        resultData: MutableMap<Any?, Any?>?
//                    ) {
//                        val imageUrl = resultData?.get("url")?.toString()
//
//                        if (imageUrl != null) {
//                            dataModel.imageURL = imageUrl
//                            Log.d("Image URL", imageUrl)
//
//                            merchViewModel.showLoading.observe(viewLifecycleOwner) { showLoading ->
//                                if (showLoading) {
//                                    dialog.show()
//                                } else {
//                                    dialog.dismiss()
//
//                                    binding.chooseSize.text = "Choose Size"
////                                    binding.choosePaymentSs.text = "Payment Screenshot"
//                                    selectedSizeIndex = 0
//                                    binding.editAddress.text.clear()
//                                    binding.editQuantity.text.clear()
//                                    selectedImageUri = null
//                                }
//                            }
//                            merchViewModel.uploadData(dataModel, requireContext(), token)
//                        } else {
//                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
//                                .show()
//                        }
//
//
//                    }
//
//                    override fun onError(requestId: String?, error: ErrorInfo?) {
//                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
//                        dialog.dismiss()
//                    }
//
//                    override fun onReschedule(requestId: String?, error: ErrorInfo?) {
//                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
//                        dialog.dismiss()
//                    }
//
//                }
//            ).dispatch()
//
//
//        }
//    }

//    private fun makepayment(){
//
//        val activity=requireActivity()
//        val co = Checkout()
//        //co.setKeyID("rzp_test_k00cTtIsQ85SIy")
//
//        try {
//            val options = JSONObject()
//            options.put("name","Srijan")
//            options.put("description","Demoing Charges")
//            //You can omit the image option to fetch the image from the dashboard
//            options.put("theme.color", "#3399cc");
//            options.put("currency","INR");
//            options.put("amount","100")//pass amount in currency subunits
//
//            val retryObj = JSONObject();
//            retryObj.put("enabled", true)
//            retryObj.put("max_count", 4);
//            options.put("retry", retryObj);
//
////            val prefill = JSONObject()
////            prefill.put("email","srijan@iitism.ac.in")
////            prefill.put("contact","8789185248")
//
////            options.put("prefill",prefill)
//            co.open(activity,options)
//        }catch (e: Exception){
//            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
//            e.printStackTrace()
//        }
//
//
//    }


    private fun startPayment(amount: Int) {
//        val checkout = Checkout()

        dataModel = DetailsDataModel()

        binding.apply {
            dataModel.apply {
                address = editAddress.text.toString().trim()
                tShirtSize = selectedSize.toString().trim()
                quantity = editQuantity.text.toString().trim()
            }
        }

        var flag = 1

        if (dataModel.address.trim().isEmpty()) {
            binding.editAddress.error = "Address can't be empty"
            Log.d("hostel", dataModel.address)
            flag = 0
        } else if (dataModel.quantity.trim().isEmpty() || dataModel.quantity.toInt() < 1) {
            binding.editQuantity.error = "Quantity  can't be empty"
            flag = 0
        } else if (isMerchSelected == 0) {
            flag = 0
            Toast.makeText(context, "Merchandise not Selected!!", Toast.LENGTH_SHORT).show()
        } else if (isSizeSelected == 0) {
            flag = 0
            Toast.makeText(context, "Size not Selected!!", Toast.LENGTH_SHORT).show()
        }
//        else if (selectedImageUri == null) {
//            flag = 0
//            Toast.makeText(context, "Image not Uploaded!!", Toast.LENGTH_SHORT).show()
//        }

        if (flag == 1 && isSizeSelected == 1 && isMerchSelected == 1) {
            val sharedPreferences =
                requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("tShirtSize", dataModel.tShirtSize)
            editor.putString("address", dataModel.address)
//            editor.putString("orderId", dataModel.orderId)
            editor.putString("quantity", dataModel.quantity)
            editor.putInt("amount", amount)
            editor.putString("userName", binding.editName.text.toString().trim())
            editor.putString("contact", "+91" + binding.editPhone.text.toString().trim())
            editor.putString("email", binding.editEmail.text.toString().trim())
            editor.putString("type", binding.chooseMerch.text.toString().trim())
            editor.apply()

            startActivity(Intent(requireContext(), MerchandiseActivity::class.java))
//            val call = RazorpayRetrofitInstance.createApi(token).makeOrder(MakeOrderBody(amount))
//
//            call.enqueue(object : retrofit2.Callback<MakeOrderResponse> {
//                override fun onResponse(
//                    call: Call<MakeOrderResponse>,
//                    response: Response<MakeOrderResponse>,
//                ) {
//                    if(response.isSuccessful) {
//                        orderId = response.body()!!.id
//                        val sharedPreferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
//                        val editor = sharedPreferences.edit()
//                        editor.putString("tShirtSize", dataModel.tShirtSize)
//                        editor.putString("address", dataModel.address)
//                        editor.putString("orderId", dataModel.orderId)
//                        editor.putString("quantity", dataModel.quantity)
//                        editor.apply()
//                        try {
////                            merchandiseListener?.sendOrder(dataModel.tShirtSize, dataModel.address, dataModel.quantity, orderId)
//
//                            val options = JSONObject()
//                            options.put("name", "Srijan '24 Merchandise")
//                            options.put("description", "Merchandise Payment")
//                            options.put("image", "https://play-lh.googleusercontent.com/bP7gDv1Uy14E1iRQdGK0ybnGmPca3tStsMqnm1ScHcY87gYOxwxRhfR4n2GWKI_sfNA=w240-h480-rw")
//                            options.put("theme.color", "#FBE10E")
//                            options.put("currency", "INR")
////            options.put("order_id", "order_DBJOWzybf0sJbx");
//                            options.put("amount", amount * 100)//pass amount in currency subunits
//                            options.put("method", JSONObject().put("upi", true))
//
//                            val retryObj = JSONObject()
//                            retryObj.put("enabled", true);
//                            retryObj.put("max_count", 4);
//                            options.put("retry", retryObj);
//
//                            checkout.open(requireActivity(), options)
//                        } catch (e: Exception) {
//                            Toast.makeText(context, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
//                            e.printStackTrace()
//                        }
//                    } else {
//                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
//                        Log.d("Merchandise Fragment", response.code().toString())
//                        Log.d("Merchandise Fragment", amount.toString())
//                    }
//                }
//
//                override fun onFailure(call: Call<MakeOrderResponse>, t: Throwable) {
//                    Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show()
//                    Log.e("EEEEEEEEEEEEEEEE", "failed to create order id")
//
//                }
//            })
//
//            try {
////                binding.progressBar.visibility = View.VISIBLE
//                val options = JSONObject()
//                options.put("name", "Srijan '24 Merchandise")
//                options.put("description", "Merchandise Payment")
//                //You can omit the image option to fetch the image from the dashboard
//                options.put(
//                    "image",
//                    "https://play-lh.googleusercontent.com/bP7gDv1Uy14E1iRQdGK0ybnGmPca3tStsMqnm1ScHcY87gYOxwxRhfR4n2GWKI_sfNA=w240-h480-rw"
//                )
//                options.put("theme.color", "#FBE10E")
//                options.put("currency", "INR")
////            options.put("order_id", "order_DBJOWzybf0sJbx");
//                options.put("amount", amount * 100)//pass amount in currency subunits
//                options.put("method", JSONObject().put("upi", true))
//
//                val retryObj = JSONObject()
//                retryObj.put("enabled", true);
//                retryObj.put("max_count", 4);
//                options.put("retry", retryObj);
//
////            val prefill = JSONObject()
////            prefill.put("email", "gaurav.kumar@example.com")
////            prefill.put("contact", "9876543210")
//
////            options.put("prefill", prefill)
//
//                checkout.open(requireActivity(), options)
//            } catch (e: Exception) {
//                Toast.makeText(context, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
//                e.printStackTrace()
//            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onPaymentSuccess(paymentId: String?) {
        Log.d("Payment", "Success!!!")
        Toast.makeText(requireContext(), "Payment is successful : $paymentId", Toast.LENGTH_SHORT)
            .show()
//        orderId = generateOrderId()
//        Log.d("Payment orderId", orderId)
//        val signature = paymentId?.let { generateRazorpaySignature(orderId, it) }
//        if (signature == null) {
//            binding.tvError.visibility = View.VISIBLE
//            binding.tvError.text = getString(R.string.payment_success_internal_server_error)
//            Log.d("Payment", "Signature fail!!!")
////            binding.progressBar.visibility = View.GONE
//        } else {
//            dialog.show()
//            Toast.makeText(context, "Payment successful", Toast.LENGTH_SHORT).show()
//
//            dataModel.orderId = orderId
//            dataModel.paymentId = paymentId
//            dataModel.signature = signature
//
//            merchViewModel.showLoading.observe(viewLifecycleOwner) { showLoading ->
//                if (showLoading) {
//                    dialog.show()
//                } else {
//                    dialog.dismiss()
//                    binding.chooseSize.text = "Choose Size"
////                                    binding.choosePaymentSs.text = "Payment Screenshot"
//                    selectedSizeIndex = 0
//                    binding.editAddress.text.clear()
//                    binding.editQuantity.text.clear()
//                }
//            }
//
//            merchViewModel.errorOccurred.observe(viewLifecycleOwner) { errorOccurred ->
//                if(errorOccurred) {
//                    binding.tvError.visibility = View.VISIBLE
//                    binding.tvError.text = getString(R.string.payment_success_internal_server_error)
//                } else {
//                    binding.tvError.visibility = View.GONE
//                }
//            }
//
//
//            merchViewModel.uploadData(dataModel, requireContext(), token)
//            Log.d("Reached here", "Reached here")
//        }
    }

    override fun onPaymentError(p0: Int, s: String?) {
        // on payment failed.
        Toast.makeText(requireContext(), "Payment Failed due to error : $s", Toast.LENGTH_SHORT)
            .show()
    }


//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }

    private fun generateRazorpaySignature(orderId: String, paymentId: String): String {
        return try {
            val data = "$orderId|$paymentId"
            val keySpec = SecretKeySpec(RAZORPAY_KEY.toByteArray(UTF_8), "HmacSHA256")
            val mac = Mac.getInstance("HmacSHA256")
            mac.init(keySpec)
            val result = mac.doFinal(data.toByteArray(UTF_8))
            bytesToHex(result)
        } catch (e: Exception) {
            // Handle exception appropriately
            e.printStackTrace()
            ""
        }
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val hexArray = "0123456789ABCDEF".toCharArray()
        val hexChars = CharArray(bytes.size * 2)
        for (i in bytes.indices) {
            val v = bytes[i].toInt() and 0xFF
            hexChars[i * 2] = hexArray[v.ushr(4)]
            hexChars[i * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }

    private fun generateOrderId(): String {
        // Use a combination of timestamp and a random number for simplicity
        val timestamp = System.currentTimeMillis()
        val random = (Math.random() * 1000).toInt()

        // Concatenate timestamp and random number to create a unique order ID
        return "$timestamp$random"
    }

}