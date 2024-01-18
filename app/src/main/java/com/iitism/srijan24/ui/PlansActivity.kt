package com.iitism.srijan24.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.iitism.srijan24.R

class PlansActivity : AppCompatActivity() {
    private lateinit var userName: String
    private lateinit var contact: String
    private lateinit var email: String
    private var amount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plans)
    }
}