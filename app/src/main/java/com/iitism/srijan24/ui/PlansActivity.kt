package com.iitism.srijan24.ui

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.iitism.srijan24.R
import com.iitism.srijan24.data.GetSignatureModel
import com.iitism.srijan24.data.GetSignatureResponse
import com.iitism.srijan24.data.MakeOrderBody
import com.iitism.srijan24.data.MakeOrderResponse
import com.iitism.srijan24.data.PlansDataModel
import com.iitism.srijan24.retrofit.RazorpayRetrofitInstance
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlansActivity : AppCompatActivity(), PaymentResultListener {
    private lateinit var dialog: Dialog

    private lateinit var orderId: String
    private lateinit var userName: String
    private lateinit var contact: String
    private lateinit var email: String
    private lateinit var token: String
    private lateinit var address: String
    private lateinit var proof: String
    private lateinit var tShirtSize: String
    private lateinit var gender: String
    private var amount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plans)

        initializeDialog()
        dialog.show()
        val sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("token", "") ?: ""

        val intent = intent

        amount = intent.getIntExtra("amount", 1199)
        userName = intent.getStringExtra("userName")!!
        contact = intent.getStringExtra("contact")!!
        email = intent.getStringExtra("email")!!
        address = intent.getStringExtra("address")!!
        proof = intent.getStringExtra("proof")!!
        tShirtSize = intent.getStringExtra("tShirtSize")!!
        gender = intent.getStringExtra("gender")!!

//        intent.putExtra("address", binding.editAddress.text.toString().trim())
//        intent.putExtra("proof", binding.editAddress.text.toString().trim())
//        intent.putExtra("gender", binding.editGender.text.toString().trim())
//        intent.putExtra("userName", userName)
//        intent.putExtra("contact", "+91$contact")
//        intent.putExtra("email", email)
//        intent.putExtra("tShirtSize", binding.chooseSize.text.toString().trim())
//        intent.putExtra("amount", amount)

        if (token.isEmpty()) {
            Toast.makeText(this, "Unauthorized access, please log in again", Toast.LENGTH_SHORT)
                .show()
            dialog.dismiss()
            finish()
        } else {
            val call = RazorpayRetrofitInstance.createApi(token).makeOrder(MakeOrderBody(amount))

            call.enqueue(object : Callback<MakeOrderResponse> {
                override fun onResponse(
                    call: Call<MakeOrderResponse>,
                    response: Response<MakeOrderResponse>
                ) {
                    if (response.isSuccessful) {
                        orderId = response.body()?.id ?: ""
                        dialog.dismiss()
                        if (orderId.isEmpty()) {
                            Toast.makeText(
                                this@PlansActivity,
                                "Something went wrong",
                                Toast.LENGTH_SHORT
                            ).show()

                            Log.d("Plans Fragment", response.code().toString())
                            Log.d("Plans Fragment", amount.toString())
                            finish()
                        } else {
                            val checkout = Checkout()
                            Log.d("Plans Order ID", orderId)
                            try {
                                val options = JSONObject()
                                options.put("name", "Srijan '24 Plans")
                                options.put("description", "Plan Payment")
                                options.put(
                                    "image",
                                    "https://res.cloudinary.com/dxomldckp/image/upload/v1705684703/srijan%2024/uxk9iaw0n4xok4jlm6qb.jpg"
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

                                checkout.open(this@PlansActivity, options)

                            } catch (e: Exception) {
                                Toast.makeText(
                                    this@PlansActivity,
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
                            this@PlansActivity,
                            "Something went wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialog.dismiss()
                        finish()
                        Log.d("Plans Fragment", response.code().toString())
                        Log.d("Plans Fragment", amount.toString())
                    }
                }

                override fun onFailure(call: Call<MakeOrderResponse>, t: Throwable) {
                    Toast.makeText(this@PlansActivity, "Failed to load data", Toast.LENGTH_SHORT)
                        .show()
                    dialog.dismiss()
                    finish()
                    Log.e("onFailure", "failed to create order id")
                }

            })
        }
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

    override fun onPaymentSuccess(paymentId: String?) {
        Toast.makeText(this, "Payment successful", Toast.LENGTH_SHORT).show()
        dialog.show()

        if (paymentId != null) {
            val call = RazorpayRetrofitInstance.createApi(token)
                .getSignature(GetSignatureModel(orderId, paymentId))

            call.enqueue(object : Callback<GetSignatureResponse> {
                override fun onResponse(
                    call: Call<GetSignatureResponse>,
                    response: Response<GetSignatureResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val signature = response.body()!!.signature

                        Toast.makeText(
                            this@PlansActivity,
                            "Payment successful",
                            Toast.LENGTH_SHORT
                        ).show()

                        val dataModel = PlansDataModel()
                        dataModel.orderId = orderId
                        dataModel.paymentId = paymentId
                        dataModel.signature = signature

                        val plan = when (amount) {
                            1999 -> "platinum"
                            1799 -> "gold"
                            1499 -> "silver"
                            1199 -> "bronze"
                            799  -> "basic"
                            else -> "essential"
                        }
                        dataModel.plan = plan

                        dataModel.address = address
                        dataModel.gender = gender
                        dataModel.tShirtSize = tShirtSize
                        dataModel.proof = proof

                        try{
                            val call2 = RazorpayRetrofitInstance.createApi(token).submitPlanDetails(dataModel)

                            call2.enqueue(object : Callback<Void> {
                                override fun onResponse(
                                    call: Call<Void>,
                                    response: Response<Void>
                                ) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(
                                            this@PlansActivity,
                                            "Plan is successfully purchased!!",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        finish()
                                    } else {
                                        Log.d("Response code", response.code().toString())
                                        when (response.code()) {
                                            403 -> {
                                                Toast.makeText(
                                                    this@PlansActivity,
                                                    "Authorization Unsuccessful",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            404 -> {
                                                Toast.makeText(
                                                    this@PlansActivity,
                                                    "Payment Successful but order not placed.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                Toast.makeText(
                                                    this@PlansActivity,
                                                    "Please contact administration for refund.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            400 -> {
                                                Toast.makeText(
                                                    this@PlansActivity,
                                                    "Payment Successful but order not placed.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                Toast.makeText(
                                                    this@PlansActivity,
                                                    "Please contact administration for refund.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                            else -> {
                                                Toast.makeText(
                                                    this@PlansActivity,
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
                                        this@PlansActivity,
                                        "Payment Successful but order not placed.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Toast.makeText(
                                        this@PlansActivity,
                                        "Please contact administration for refund.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                }
                            }
                            )


                        } catch (e: Exception){
                            Log.d("Plans", e.toString())
                        }

                    } else {
                        Log.d("Response code signature", response.code().toString())

                    }
                }

                override fun onFailure(call: Call<GetSignatureResponse>, t: Throwable) {
                    Log.i("Tag", t.toString())
                    Toast.makeText(
                        this@PlansActivity,
                        "Payment Successful but order not placed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Toast.makeText(
                        this@PlansActivity,
                        "Please contact administration for refund.",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }

            })
        }

    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show()
        finish()
    }
}