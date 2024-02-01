package com.iitism.srijan24.ui

import EventDataModel
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.iitism.srijan24.R
import com.iitism.srijan24.adapter.ContactAdapter
import com.iitism.srijan24.data.EventTeamModel
import com.iitism.srijan24.data.MemberDataModel
import com.iitism.srijan24.data.MemberListModel
import com.iitism.srijan24.databinding.FragmentAboutEventBinding
import com.iitism.srijan24.view_model.RegistrationViewModel

class AboutEventFragment : Fragment() {

    private var _binding: FragmentAboutEventBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventData: EventDataModel

    private lateinit var adapter: ContactAdapter

    private lateinit var dialog: Dialog

    private lateinit var viewModel: RegistrationViewModel
    private lateinit var token: String

    val url = "https://forms.gle/DVKjyW2WNECmTUVb6"
    val intent = Intent(Intent.ACTION_VIEW)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeDialog()

        arguments?.let {
            eventData = it.getParcelable("eventData")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAboutEventBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[RegistrationViewModel::class.java]
        return binding.root
    }

    private var ind = 0
    private var memberData = mutableListOf<MemberDataModel>()
    private var registerData = EventTeamModel()

    private var addedMemberCount = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tveventName.text = eventData.eventName
        binding.tvEventFee.text = eventData.fees
        binding.tvVenue.text = eventData.venue
        binding.tvEventDescription.text = eventData.description

        if (eventData.maxMembers!!.toInt() <= 1) {
            binding.ltTeamName.visibility = View.GONE
        }

        if (eventData.zone != "Music" && eventData.zone != "Dance") {
            binding.ltAudio.visibility = View.GONE
        }

        if (eventData.zone != "Music" && eventData.zone != "Dance") {
            binding.ltAccompanist.visibility = View.GONE
        }

        for (i in 1..eventData.minMembers!!.toInt()) {
            val layout = LayoutInflater.from(requireContext())
                .inflate(R.layout.layout_event_member_schema, binding.root, false)

            val localInd = ind
            memberData += MemberDataModel()

            layout.findViewById<TextView>(R.id.tvMemberNumber).text = "Member $i"

            if (eventData.zone != "Music" && eventData.zone != "Dance") {
                layout.findViewById<LinearLayout>(R.id.ltInstrument).visibility = View.GONE
            }

            layout.findViewById<EditText>(R.id.etName).addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    memberData[localInd].name = s.toString().trim()
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })

            layout.findViewById<EditText>(R.id.etEmail)
                .addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        memberData[localInd].email = s.toString().trim()
                    }

                    override fun afterTextChanged(s: Editable?) {

                    }
                })

            layout.findViewById<EditText>(R.id.etPhoneNumber)
                .addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        memberData[localInd].phone = s.toString().trim()
                    }

                    override fun afterTextChanged(s: Editable?) {

                    }
                })

            layout.findViewById<EditText>(R.id.etCollege)
                .addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        memberData[localInd].college = s.toString().trim()
                    }

                    override fun afterTextChanged(s: Editable?) {

                    }
                })

            layout.findViewById<EditText>(R.id.etInstrument)
                .addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        memberData[localInd].instrument = s.toString().trim()
                    }

                    override fun afterTextChanged(s: Editable?) {

                    }
                })

            ind++

            binding.ltMembers.addView(layout)
        }

        binding.btnRemoveMembers.visibility = View.GONE

        binding.btnAddMembers.setOnClickListener {
            if (ind != eventData.maxMembers!!.toInt()) {
                val layout = LayoutInflater.from(requireContext())
                    .inflate(R.layout.layout_event_member_schema, binding.root, false)

                val localInd = ind
                memberData += MemberDataModel()

                layout.findViewById<TextView>(R.id.tvMemberNumber).text = "Member ${localInd + 1}"

                if (eventData.zone != "Music" && eventData.zone != "Dance") {
                    layout.findViewById<LinearLayout>(R.id.ltInstrument).visibility = View.GONE
                }

                layout.findViewById<EditText>(R.id.etName)
                    .addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {

                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            memberData[localInd].name = s.toString().trim()
                        }

                        override fun afterTextChanged(s: Editable?) {

                        }
                    })

                layout.findViewById<EditText>(R.id.etEmail)
                    .addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {

                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            memberData[localInd].email = s.toString().trim()
                        }

                        override fun afterTextChanged(s: Editable?) {

                        }
                    })

                layout.findViewById<EditText>(R.id.etPhoneNumber)
                    .addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {

                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            memberData[localInd].phone = s.toString().trim()
                        }

                        override fun afterTextChanged(s: Editable?) {

                        }
                    })

                layout.findViewById<EditText>(R.id.etCollege)
                    .addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {

                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            memberData[localInd].college = s.toString().trim()
                        }

                        override fun afterTextChanged(s: Editable?) {

                        }
                    })

                layout.findViewById<EditText>(R.id.etInstrument)
                    .addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {

                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            memberData[localInd].instrument = s.toString().trim()
                        }

                        override fun afterTextChanged(s: Editable?) {

                        }
                    })

                ind++

                binding.ltMembers.addView(layout)
                addedMemberCount++
                if(binding.btnRemoveMembers.visibility != View.VISIBLE)
                    binding.btnRemoveMembers.visibility = View.VISIBLE
            } else {
                showSnackBar("Maximum members reached")
//                Toast.makeText(context, "Maximum members reached", Toast.LENGTH_SHORT).show()
            }
        }

        if (eventData.minMembers == eventData.maxMembers)
            binding.btnAddMembers.visibility = View.GONE

        binding.btnRemoveMembers.setOnClickListener {
            val childCount = binding.ltMembers.childCount
            if(childCount > 0)
                binding.ltMembers.removeViewAt(childCount - 1)

            memberData.removeLast()

            addedMemberCount--
            ind--
            if(addedMemberCount == 0)
                binding.btnRemoveMembers.visibility = View.GONE
        }

        binding.btnSubmit.setOnClickListener {
            submitData()
        }

        adapter = ContactAdapter(eventData.contact!!, requireContext())
        binding.rvContact.layoutManager = LinearLayoutManager(requireContext())
        binding.rvContact.adapter = adapter
        binding.rvContact.setHasFixedSize(true)

        Glide.with(requireContext())
            .load(eventData.poster)
            .placeholder(R.drawable.progress_animation)
            .error(R.drawable.progress_animation)
            .into(binding.eventImage)

        binding.pdfLink.setOnClickListener {
            if (eventData.ruleBookLink != null) {
                val url = eventData.ruleBookLink
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                intent.setPackage("com.android.chrome")
                Log.i("pdflink", intent.data.toString())
                startActivity(intent)

            } else
                showSnackBar("No PDF Link available")
//                Toast.makeText(context, "No PDF Link available", Toast.LENGTH_SHORT).show()
        }

        binding.btnRegister.setOnClickListener {

            val preferences = requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
            val isISMite = preferences.getString("isISMite", "") ?: ""

            if(isISMite=="false"){
                intent.data = Uri.parse(url)
                intent.setPackage("com.android.chrome")
                startActivity(intent)
            }else{
                if(eventData.maxMembers != null && eventData.maxMembers!!.toInt() == 0) {
                    Toast.makeText(context, "No registration required", Toast.LENGTH_SHORT).show()
                } else {
                    binding.cvRegister.visibility = View.VISIBLE
                    binding.btnRegister.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initializeDialog() {
        dialog = Dialog(requireContext())
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
                        R.color.progress_bar
                    )
                )
            )
        }
    }

    private fun submitData() {
        var check = true
        if (eventData.maxMembers!!.toInt() > 1 && binding.etTeamName.text.toString().trim()
                .isEmpty()
        ) {
            check = false
        }

        for (member in memberData) {
            if (member.name.isNullOrEmpty() || member.email.isNullOrEmpty() || member.phone.isNullOrEmpty() || member.college.isNullOrEmpty()) {
                check = false
                break
            }
        }

        if (!check) {
            showSnackBar("Please fill all the mandatory details")
//            Toast.makeText(context, "Please fill all the mandatory details", Toast.LENGTH_SHORT)
//                .show()
        } else {
            dialog.show()

            registerData.eventName = eventData.eventName!!
            registerData.teams += arrayOf(MemberListModel())
            Log.d("aaaaaaaaaaaaaaaaaaaaa", registerData.teams.size.toString())
            registerData.teams[0].teamName = binding.etTeamName.text.toString().trim()
            if (eventData.zone == "Music" || eventData.zone == "Dance") {
                registerData.teams[0].audio = binding.etAudio.text.toString().trim()
                registerData.teams[0].accompanist = binding.etAccompanist.text.toString().trim()
            }
            registerData.teams[0].memberList = memberData

            val preferences =
                requireActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
            token = preferences.getString("token", "") ?: ""

            if (token.isEmpty()) {
                dialog.dismiss()
                showSnackBar("User session expired. Please re-login")
//                Toast.makeText(context, "User session expired. Please re-login", Toast.LENGTH_SHORT)
//                    .show()
            } else {
                viewModel.registerData(registerData, token) { code ->
                    dialog.dismiss()
                    when (code) {
                        200 -> {
                            showSnackBar("Successfully registered for event!!!")
//                            Toast.makeText(context, "Successfully registered for event!!!", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }

                        1000 -> {
                            showSnackBar("Couldn't send request")
//                            Toast.makeText(context, "Couldn't send request", Toast.LENGTH_SHORT)
//                                .show()
                        }

                        403 -> {
                            showSnackBar("User Authentication failed. Please re-login")
//                            Toast.makeText(context, "User Authentication failed. Please re-login", Toast.LENGTH_SHORT).show()
                        }

                        404 -> {
                            showSnackBar("Every participant should be registered and must have a plan for events")
//                            Toast.makeText(context, "Every participant should be registered and must have a plan for events", Toast.LENGTH_SHORT).show()
//                            if(viewModel.errorMessage.isNotEmpty()) {
//                                Toast.makeText(context, viewModel.errorMessage, Toast.LENGTH_SHORT).show()
//                            } else {
//                                Toast.makeText(context, "Every participant should be registered and must have a plan", Toast.LENGTH_SHORT).show()
//                            }
                        }

                        else -> {
                            showSnackBar("Unexpected error occurred")
//                            Toast.makeText(context, "Unexpected error occurred", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun showSnackBar(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
    }
}