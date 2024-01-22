package com.iitism.srijan24.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.iitism.srijan24.R
import com.iitism.srijan24.data.GetUserResponse
import com.iitism.srijan24.databinding.FragmentPlansBinding
import com.iitism.srijan24.retrofit.UserApiInstance
import retrofit2.Call
import retrofit2.Response

class PlansFragment : Fragment() {
    private var _binding: FragmentPlansBinding? = null
    private val binding get() = _binding!!
    private lateinit var dialog: Dialog

    private lateinit var userName: String
    private lateinit var contact: String
    private lateinit var email: String

    private var amount = 0
    private lateinit var token: String

    private var isSizeSelected = 0
    private var isMerchSelected = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlansBinding.inflate(inflater, container, false)
        initializeDialog()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val preferences =
            requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        token = preferences.getString("token", "") ?: ""

        if (token.isEmpty()) {
            findNavController().navigate(R.id.action_plansFragmentDrawer_to_homeFragment)
            startActivity(Intent(requireContext(), LoginSignupActivity::class.java))
        } else {
            val call = UserApiInstance.createUserApi(token).getUser()

            dialog.show()
            call.enqueue(object : retrofit2.Callback<GetUserResponse> {
                override fun onResponse(
                    call: Call<GetUserResponse>,
                    response: Response<GetUserResponse>,
                ) {
                    dialog.dismiss()
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        userName = body.name
                        contact = body.phoneNumber
                        email = body.email

                        binding.editName.setText(userName)
                        binding.editPhone.setText(contact)
                        binding.editEmail.setText(email)

                    } else {
                        Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                }

                override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                    dialog.dismiss()
                    Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show()
                    Log.e("EEEEEEEEEEEEEEEE", t.toString())
                    findNavController().popBackStack()
                }
            })
        }


//        val intent = Intent(requireContext(), PlansActivity::class.java)
//        intent.putExtra("amount", amount)
        binding.btnPlatinum.setOnClickListener {
            if(binding.cvDetails.visibility != View.VISIBLE)
                binding.cvDetails.visibility = View.VISIBLE

            if(binding.ltAddress.visibility != View.VISIBLE)
                binding.ltAddress.visibility = View.VISIBLE

            if(binding.chooseMerch.visibility != View.VISIBLE)
                binding.chooseMerch.visibility = View.VISIBLE

            if(binding.chooseSize.visibility != View.VISIBLE)
                binding.chooseSize.visibility = View.VISIBLE

            amount = 1999
            binding.totalPrice.text = "Payable Amount: \\u20B9 1999".unescapeUnicode()
            binding.scrollview.post {
                binding.scrollview.fullScroll(View.FOCUS_DOWN)
            }
        }

        binding.btnGold.setOnClickListener {
            if(binding.cvDetails.visibility != View.VISIBLE)
                binding.cvDetails.visibility = View.VISIBLE

            if(binding.ltAddress.visibility != View.VISIBLE)
                binding.ltAddress.visibility = View.VISIBLE

            if(binding.chooseMerch.visibility != View.VISIBLE)
                binding.chooseMerch.visibility = View.VISIBLE

            if(binding.chooseSize.visibility != View.VISIBLE)
                binding.chooseSize.visibility = View.VISIBLE

            amount = 1799
            binding.totalPrice.text = "Payable Amount: \\u20B9 1799".unescapeUnicode()
            binding.scrollview.post {
                binding.scrollview.fullScroll(View.FOCUS_DOWN)
            }
//            intent.putExtra("amount", amount)
//            startActivity(intent)

        }

        binding.btnSilver.setOnClickListener {
            if(binding.cvDetails.visibility != View.VISIBLE)
                binding.cvDetails.visibility = View.VISIBLE

            if(binding.ltAddress.visibility != View.GONE)
                binding.ltAddress.visibility = View.GONE

            if(binding.chooseMerch.visibility != View.GONE)
                binding.chooseMerch.visibility = View.GONE

            if(binding.chooseSize.visibility != View.GONE)
                binding.chooseSize.visibility = View.GONE

            amount = 1499
            binding.totalPrice.text = "Payable Amount: \\u20B9 1499".unescapeUnicode()
            binding.scrollview.post {
                binding.scrollview.fullScroll(View.FOCUS_DOWN)
            }
//            intent.putExtra("amount", amount)
//            startActivity(intent)
        }

        binding.btnBronze.setOnClickListener {
            if(binding.cvDetails.visibility != View.VISIBLE)
                binding.cvDetails.visibility = View.VISIBLE

            if(binding.ltAddress.visibility != View.GONE)
                binding.ltAddress.visibility = View.GONE

            if(binding.chooseMerch.visibility != View.GONE)
                binding.chooseMerch.visibility = View.GONE

            if(binding.chooseSize.visibility != View.GONE)
                binding.chooseSize.visibility = View.GONE

            amount = 1199
            binding.totalPrice.text = "Payable Amount: \\u20B9 1199".unescapeUnicode()
            binding.scrollview.post {
                binding.scrollview.fullScroll(View.FOCUS_DOWN)
            }
//            intent.putExtra("amount", amount)
//            startActivity(intent)

        }

        binding.chooseMerch.setOnClickListener {
            showMerchMenu()
        }

        binding.editGender.setOnClickListener {
            showGenderMenu()
        }

        binding.chooseSize.setOnClickListener {
            if(isMerchSelected == 1) showSizeMenu()
            else Toast.makeText(context, "Please select merchandise first", Toast.LENGTH_SHORT).show()
        }

        binding.payButton.setOnClickListener {
            if((amount == 1999 || amount == 1799) && binding.editAddress.text.toString().trim().isEmpty()) {
                binding.editAddress.error = "Please enter the address"
            } else if(binding.editProof.text.toString().trim().isEmpty()) {
                binding.editProof.error = "Please enter your ID Proof"
            } else if(binding.editGender.text.toString().trim().isEmpty()) {
                binding.editGender.error = "Please enter your gender"
            } else if((amount == 1999 || amount == 1799) && isMerchSelected == 0) {
                Toast.makeText(context, "Please choose from available merchandise", Toast.LENGTH_SHORT).show()
            } else if((amount == 1999 || amount == 1799) && isSizeSelected == 0) {
                Toast.makeText(context, "Please choose from available sizes", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(requireContext(), PlansActivity::class.java)
                intent.putExtra("address", binding.editAddress.text.toString().trim())
                intent.putExtra("proof", binding.editAddress.text.toString().trim())
                intent.putExtra("gender", binding.editGender.text.toString().trim())
                intent.putExtra("userName", userName)
                intent.putExtra("contact", "+91$contact")
                intent.putExtra("email", email)
                intent.putExtra("tShirtSize", binding.chooseSize.text.toString().trim())
                intent.putExtra("amount", amount)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (token.isEmpty()) {
            findNavController().navigate(R.id.action_merchandiseFragment_to_homeFragment)
            startActivity(Intent(requireContext(), LoginSignupActivity::class.java))
        }
    }

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

    private fun String.unescapeUnicode(): String {
        return Regex("""\\u([0-9a-fA-F]{4})""")
            .replace(this) {
                it.groupValues[1].toInt(16).toChar().toString()
            }
    }

    private var selectedGenderIndex = 0
    private var selectedGender: String? = null
    private fun showGenderMenu() {
        val customViewMerch = layoutInflater.inflate(R.layout.layout_custom_material_dialog, null)
        val materialDialogMerch =
            MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog).setView(
                customViewMerch
            ).show()

        val radioGroup = materialDialogMerch.findViewById<RadioGroup>(R.id.customDialogRadioGroup)!!

        val merchandiseArray = arrayOf("Male", "Female")
        selectedGenderIndex = 0
        selectedGender = merchandiseArray[selectedGenderIndex]
        for (i in merchandiseArray.indices) {
            val radioButton = layoutInflater.inflate(
                R.layout.layout_custom_material_dialog_radio_button,
                null
            ) as RadioButton
            radioButton.text = merchandiseArray[i]
            radioButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            radioGroup.addView(radioButton)

            if (i == selectedGenderIndex) {
                radioButton.isChecked = true
            }

            radioButton.tag = i

            radioButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedGenderIndex = radioButton.tag as Int
                    selectedGender = merchandiseArray[selectedGenderIndex]
                }
            }
        }

        materialDialogMerch.findViewById<TextView>(R.id.customDialogTitle)?.text =
            "Choose Gender"

        val positiveButton = materialDialogMerch.findViewById<Button>(R.id.customDialogPositiveBtn)
        positiveButton?.apply {
            setOnClickListener {
                showSnackBar("$selectedGender selected")
                binding.editGender.text = merchandiseArray[selectedGenderIndex]
                isMerchSelected = 1
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

    private var selectedSizeIndex = 0
    private var selectedSize: String? = null
    private fun showSizeMenu() {
        val customView = layoutInflater.inflate(R.layout.layout_custom_material_dialog, null)
        val materialDialog = MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setView(customView)
            .show()

        // Find the RadioGroup in the layout
        val radioGroup = materialDialog.findViewById<RadioGroup>(R.id.customDialogRadioGroup)!!

        val merchSize = arrayOf("XS", "S", "M", "L", "XL", "2XL")
        selectedSize = "XS"
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

    private var selectedMerchIndex = 0
    private var selectedMerch: String? = null
    private fun showMerchMenu() {
        val customViewMerch = layoutInflater.inflate(R.layout.layout_custom_material_dialog, null)
        val materialDialogMerch =
            MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
                .setView(customViewMerch)
                .show()

        // Find the RadioGroup in the layout
        val radioGroup = materialDialogMerch.findViewById<RadioGroup>(R.id.customDialogRadioGroup)!!

        // Add radio buttons dynamically based on the array of options
        val merchandiseArray = arrayOf("T-Shirt")
        selectedMerchIndex = 0
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

        materialDialogMerch.findViewById<TextView>(R.id.customDialogTitle)?.text =
            "Available Merchandise"

        // Find and set onClickListeners for buttons
        val positiveButton = materialDialogMerch.findViewById<Button>(R.id.customDialogPositiveBtn)
        positiveButton?.apply {
            text = "OK" // Set the button text if needed
            setOnClickListener {
                showSnackBar("$selectedMerch selected")
                binding.chooseMerch.text = merchandiseArray[selectedMerchIndex]
                isMerchSelected = 1
                isSizeSelected = 0
                binding.chooseSize.text = "Choose Size"
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
}