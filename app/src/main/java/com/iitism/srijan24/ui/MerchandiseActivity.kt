package com.iitism.srijan24.ui

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.iitism.srijan24.BuildConfig
import com.iitism.srijan24.R
import com.iitism.srijan24.data.DetailsDataModel
import com.iitism.srijan24.data.GetSignatureModel
import com.iitism.srijan24.data.GetSignatureResponse
import com.iitism.srijan24.data.MakeOrderBody
import com.iitism.srijan24.data.MakeOrderResponse
import com.iitism.srijan24.retrofit.MerchandiseRetrofitInstance
import com.iitism.srijan24.retrofit.RazorpayRetrofitInstance
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class MerchandiseActivity : AppCompatActivity(), PaymentResultListener {
    private lateinit var dialog: Dialog

    private lateinit var tShirtSize: String
    private lateinit var address: String
    private lateinit var quantity: String
    private lateinit var orderId: String
    private lateinit var token: String
    private lateinit var userName: String
    private lateinit var contact: String
    private lateinit var email: String
    private lateinit var type: String
    private var amount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merchandise)
        initializeDialog()
        dialog.show()

        val sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        quantity = sharedPreferences.getString("quantity", "quantity")!!
        tShirtSize = sharedPreferences.getString("tShirtSize", "tShirtSize")!!
        address = sharedPreferences.getString("address", "address")!!
        token = sharedPreferences.getString("token", "") ?: ""
        amount = sharedPreferences.getInt("amount", 399)
        userName = sharedPreferences.getString("userName", "")!!
        contact = sharedPreferences.getString("contact", "")!!
        email = sharedPreferences.getString("email", "")!!
        type = sharedPreferences.getString("type", "")!!

        if (token.isEmpty()) {
            Toast.makeText(this, "Unauthorized access, please log in again", Toast.LENGTH_SHORT)
                .show()
            dialog.dismiss()
            finish()
        }

        val call = RazorpayRetrofitInstance.createApi(token).makeOrder(MakeOrderBody(amount))

        call.enqueue(object : retrofit2.Callback<MakeOrderResponse> {
            override fun onResponse(
                call: Call<MakeOrderResponse>,
                response: Response<MakeOrderResponse>,
            ) {
                if (response.isSuccessful) {
                    orderId = response.body()?.id ?: ""
                    dialog.dismiss()
                    if (orderId.isEmpty()) {
                        Toast.makeText(
                            this@MerchandiseActivity,
                            "Something went wrong",
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.d("Merchandise Fragment", response.code().toString())
                        Log.d("Merchandise Fragment", amount.toString())
                        finish()
                    } else {
                        val checkout = Checkout()
                        Log.d("Merch real merch!!!", orderId)
                        try {
//                            merchandiseListener?.sendOrder(dataModel.tShirtSize, dataModel.address, dataModel.quantity, orderId)
                            val options = JSONObject()
                            options.put("name", "Srijan '24 Merchandise")
                            options.put("description", "Merchandise Payment")
                            options.put(
                                "image",
                                "https://play-lh.googleusercontent.com/bP7gDv1Uy14E1iRQdGK0ybnGmPca3tStsMqnm1ScHcY87gYOxwxRhfR4n2GWKI_sfNA=w240-h480-rw"
                            )
                            options.put("theme.color", "#FBE10E")
                            options.put("prefill.name", userName)
                            options.put("prefill.contact", contact)
                            options.put("prefill.email", email)
                            options.put("currency", "INR")
                            options.put("amount", amount * 100)//pass amount in currency subunits
                            options.put("method", JSONObject().put("upi", true))

                            val retryObj = JSONObject()
                            retryObj.put("enabled", true)
                            retryObj.put("max_count", 4)
                            options.put("retry", retryObj)

                            checkout.open(this@MerchandiseActivity, options)
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@MerchandiseActivity,
                                "Something went wrong",
                                Toast.LENGTH_LONG
                            ).show()
                            dialog.dismiss()
                            finish()
                            e.printStackTrace()
                        }
                    }

                } else {
                    Toast.makeText(
                        this@MerchandiseActivity,
                        "Something went wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog.dismiss()
                    finish()
                    Log.d("Merchandise Fragment", response.code().toString())
                    Log.d("Merchandise Fragment", amount.toString())
                }
            }

            override fun onFailure(call: Call<MakeOrderResponse>, t: Throwable) {
                Toast.makeText(this@MerchandiseActivity, "Failed to load data", Toast.LENGTH_SHORT)
                    .show()
                dialog.dismiss()
                finish()
                Log.e("EEEEEEEEEEEEEEEE", "failed to create order id")

            }
        })
    }

    private fun initializeDialog() {
        dialog = Dialog(this)
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
                        this,
                        R.color.progress_bar
                    )
                )
            )
        }
    }

    private fun generateRazorpaySignature(orderId: String, paymentId: String): String {
        return try {
            val data = "$orderId|$paymentId"
            val keySpec =
                SecretKeySpec(BuildConfig.RAZORPAY_KEY.toByteArray(Charsets.UTF_8), "HmacSHA256")
            val mac = Mac.getInstance("HmacSHA256")
            mac.init(keySpec)
            val result = mac.doFinal(data.toByteArray(Charsets.UTF_8))
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

    override fun onPaymentSuccess(paymentId: String?) {
        Toast.makeText(this, "Payment successful", Toast.LENGTH_SHORT).show()
        dialog.show()

        if (paymentId != null) {
            val call = RazorpayRetrofitInstance.createApi(token)
                .getSignature(GetSignatureModel(orderId, paymentId))

            call.enqueue(object : retrofit2.Callback<GetSignatureResponse> {
                override fun onResponse(
                    call: Call<GetSignatureResponse>,
                    response: Response<GetSignatureResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val signature = response.body()!!.signature

                        Toast.makeText(
                            this@MerchandiseActivity,
                            "Payment successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        val dataModel = DetailsDataModel()
                        dataModel.orderId = orderId
                        dataModel.paymentId = paymentId
                        dataModel.signature = signature
                        dataModel.address = address
                        dataModel.quantity = quantity
                        dataModel.tShirtSize = tShirtSize
                        dataModel.type = type

                        try {

                            val call2 = RazorpayRetrofitInstance.createApi(token)
                                .submitOrderDetails(dataModel)
                            call2.enqueue(object : retrofit2.Callback<Void> {
                                override fun onResponse(
                                    call: Call<Void>,
                                    response: Response<Void>
                                ) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(
                                            this@MerchandiseActivity,
                                            "Order is successfully placed!!",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        finish()
                                    } else {
                                        Log.d("Response code", response.code().toString())
                                        when (response.code()) {
                                            403 -> {
                                                Toast.makeText(
                                                    this@MerchandiseActivity,
                                                    "Authorization Unsuccessful",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                            400 -> {
                                                Toast.makeText(
                                                    this@MerchandiseActivity,
                                                    "Something went wrong",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                            else -> {
                                                Toast.makeText(
                                                    this@MerchandiseActivity,
                                                    "Unexpected error occurred",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                        dialog.dismiss()
                                        finish()
                                    }
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    Log.i("onFailure", t.toString())
                                    Toast.makeText(
                                        this@MerchandiseActivity,
                                        "Try again !!, It may happen first time",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                }
                            }
                            )

                        } catch (e: IOException) {
                            Log.d("MerchandiseViewModel", e.toString())
                        }
                    } else {
                        Log.d("Response code signature", response.code().toString())

                    }
                }

                override fun onFailure(call: Call<GetSignatureResponse>, t: Throwable) {
                    Log.i("Tag", t.toString())
                    Toast.makeText(
                        this@MerchandiseActivity,
                        "Payment Successful but order not placed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Toast.makeText(
                        this@MerchandiseActivity,
                        "Please contact administration for refund.",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()

                }
            }
            )
        }


//        val signature = paymentId?.let { generateRazorpaySignature(orderId, it) }
//        if (signature == null) {
//            Log.d("Payment", "Signature fail!!!")
//            dialog.dismiss()
//            finish()
////            binding.progressBar.visibility = View.GONE
//        } else {
//
//
////            merchViewModel.showLoading.observe(viewLifecycleOwner) { showLoading ->
////                if (showLoading) {
////                    dialog.show()
////                } else {
////                    dialog.dismiss()
////                    binding.chooseSize.text = "Choose Size"
//////                                    binding.choosePaymentSs.text = "Payment Screenshot"
////                    selectedSizeIndex = 0
////                    binding.editAddress.text.clear()
////                    binding.editQuantity.text.clear()
////                }
////            }
////
////            merchViewModel.errorOccurred.observe(viewLifecycleOwner) { errorOccurred ->
////                if(errorOccurred) {
////                    binding.tvError.visibility = View.VISIBLE
////                    binding.tvError.text = getString(R.string.payment_success_internal_server_error)
////                } else {
////                    binding.tvError.visibility = View.GONE
////                }
////            }
//
//
////            merchViewModel.uploadData(dataModel, requireContext(), token)
////            Log.d("Reached here", "Reached here")
//        }
//        finish()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show()
        finish()
    }
}