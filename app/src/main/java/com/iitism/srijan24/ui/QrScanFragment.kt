package com.iitism.srijan24.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.iitism.srijan24.R
import com.iitism.srijan24.databinding.FragmentLoginBinding
import com.iitism.srijan24.databinding.FragmentQrScanBinding

class QrScanFragment : Fragment() {

    private var _binding: FragmentQrScanBinding? = null
    private val binding get() = _binding!!
    private lateinit var codeScanner: CodeScanner
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentQrScanBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scannerView = binding.scannerView

        codeScanner = CodeScanner(requireActivity(), scannerView)

        binding.btnScanqr.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.CAMERA),
                    1001
                )

            } else {
                scanQR()
            }

        }
    }

    private fun scanQR() {
        binding.btnScanqr.visibility = View.GONE
        binding.cardDetailsQr.visibility = View.GONE
        binding.scannerView.visibility = View.VISIBLE


        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {

            activity?.runOnUiThread() {
//                    Toast.makeText(requireContext(), "Scan result: ${it.text}", Toast.LENGTH_LONG)
//                        .show()
                binding.scannerView.visibility = View.GONE
                binding.cardDetailsQr.visibility = View.VISIBLE
                binding.btnScanqr.visibility = View.VISIBLE
                val result = it.text
                var data = ""
                val dataList = arrayListOf<String>()

                try {
                    for (i in result) {
                        if (i != ';') {
                            data += i
                        } else {
                            dataList.add(data)
                            data = ""
                        }
                    }
                    Log.d("TAG", dataList.toString())
                    val nameQr = "Name: " + dataList[0]
                    val emailQr = "Email: " + dataList[1]
                    val contactQr = "Contact: " + dataList[2]
                    val collegeQr =
                        "College: " + if (dataList[3] == "true") "IIT(ISM) Dhanbad" else "Others"
                    binding.tvNameQr.text = nameQr
                    binding.tvEmailQr.text = emailQr
                    binding.tvContactQr.text = contactQr
                    binding.tvCollegeQr.text = collegeQr

                } catch(e: Exception){
                    Toast.makeText(requireContext(), "Invalid QR Code", Toast.LENGTH_SHORT).show()
                }
            }


        }

        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            activity?.runOnUiThread {
                Toast.makeText(
                    requireContext(), "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()

                binding.scannerView.visibility = View.GONE
                binding.cardDetailsQr.visibility = View.VISIBLE
                binding.btnScanqr.visibility = View.VISIBLE
            }
        }
        codeScanner.startPreview()

    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}