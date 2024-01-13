package com.iitism.srijan24.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.iitism.srijan24.R
import com.iitism.srijan24.data.AddTokenModel
import com.iitism.srijan24.databinding.ActivityMainBinding
import com.iitism.srijan24.retrofit.TokenApi
import com.iitism.srijan24.retrofit.TokenRetrofitInstance
import com.razorpay.PaymentResultListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Timer


class MainActivity : AppCompatActivity(), PaymentResultListener {
    private val topic = "/topics/announcement"
    private lateinit var binding: ActivityMainBinding
    private lateinit var dialog: Dialog

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeDialog()
        askNotificationPermission()

        dialog.show()
        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("SUBSCRIBE", "subscribed")
            } else {
                Log.d("SUBSCRIBE", "subscription failed")
            }
            dialog.dismiss()
        }

//        val fcmToken = getFCMTokenFromSharedPreferences()
//        if(fcmToken.isEmpty()) {
//            dialog.show()
//            obtainAndSendFCMTokenToApi()
//        }

        navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.eventsFragment,
                R.id.announcementsFragment,
                R.id.merchandiseFragment,
                R.id.sponsorsFragment,
                R.id.addAnnouncementFragment,
                R.id.profileFragment,
                R.id.aboutUsFragment,
                R.id.coreTeamFragment,
                R.id.contactFragment
            ), binding.drawerLayout
        )

//        setupActionBarWithNavController(navController, binding.drawerLayout)
//        binding.navView.setupWithNavController(navController)

        if (navController.currentDestination?.id == R.id.homeFragment) {
            binding.appBar.btnProfile.visibility = View.VISIBLE
            binding.appBar.btnProfile.setOnClickListener {
                val preferences =
                    getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
//                val isISMite = preferences.getString("isISMite", "") ?: ""
                val userId = preferences.getString("userId", "") ?: ""

                if (userId.isEmpty()) {
                    startActivity(Intent(this, LoginSignupActivity::class.java))
                } else{
                navController.navigate(R.id.action_homeFragment_to_profileFragment)
            }}
        }


        binding.appBar.btnMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.homeFragment -> binding.navView.setCheckedItem(R.id.homeFragment)
                R.id.eventsFragment -> binding.navView.setCheckedItem(R.id.eventsFragment)
                R.id.announcementsFragment -> binding.navView.setCheckedItem(R.id.announcementsFragment)
                R.id.merchandiseFragment -> binding.navView.setCheckedItem(R.id.merchandiseFragment)
                R.id.sponsorsFragment -> binding.navView.setCheckedItem(R.id.sponsorsFragment)
                R.id.addAnnouncementFragment -> binding.navView.setCheckedItem(R.id.addAnnouncementFragment)
                R.id.profileFragment -> binding.navView.setCheckedItem(R.id.profileFragment)
                R.id.aboutUsFragment -> binding.navView.setCheckedItem(R.id.aboutUsFragment)
                R.id.coreTeamFragment -> binding.navView.setCheckedItem(R.id.coreTeamFragment)
                R.id.contactFragment -> binding.navView.setCheckedItem(R.id.contactFragment)
                else -> binding.navView.setCheckedItem(R.id.homeFragment)
            }
            binding.appBar.tvTitle.text = when (destination.id) {
                R.id.homeFragment -> "Home"
                R.id.eventsFragment -> "Events"
                R.id.announcementsFragment -> "Announcements"
                R.id.merchandiseFragment -> "Merchandise"
                R.id.sponsorsFragment -> "Past Sponsors"
                R.id.addAnnouncementFragment -> "New Announcement"
                R.id.profileFragment -> "Profile"
                R.id.aboutUsFragment -> "About Us"
                R.id.coreTeamFragment -> "Core Team"
                R.id.contactFragment -> "Contact Us"
                else -> "Srijan 24"
            }

            if (destination.id != R.id.homeFragment) binding.appBar.btnProfile.visibility =
                View.GONE
            else binding.appBar.btnProfile.visibility = View.VISIBLE
        }

        binding.navView.setupWithNavController(navController)
        binding.navView.setCheckedItem(R.id.homeFragment)
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
                        R.color.bg
                    )
                )
            )
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
            //extract token

        } else {
            Toast.makeText(this, "Notifications will not be shown", Toast.LENGTH_SHORT).show()
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun getFCMTokenFromSharedPreferences(): String {
        val preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        return preferences.getString("fcmToken", "") ?: ""
    }

    private fun saveFCMTokenToSharedPreferences(token: String) {
        val preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        preferences.edit().putString("fcmToken", token).apply()
    }

    private fun obtainAndSendFCMTokenToApi() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                sendToken(token)
            } else {
                Log.e("FCM Token", "Failed to get token", task.exception)
                dialog.dismiss()
            }
        }
    }

    private fun sendToken(token: String) {
        val tokenApi: TokenApi = TokenRetrofitInstance.tokenApi
        val addTokenCall: Call<Void> = tokenApi.addToken(AddTokenModel(token))

        addTokenCall.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    saveFCMTokenToSharedPreferences(token)
                    Log.d("Token sent successfully", token)
                    dialog.dismiss()
                } else {
                    dialog.dismiss()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("AddToken", "Network request failed", t)
                dialog.dismiss()
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(applicationContext, "Payment success", Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(applicationContext, "Payment failure", Toast.LENGTH_SHORT).show()
    }


}